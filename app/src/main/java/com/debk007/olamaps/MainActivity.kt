package com.debk007.olamaps

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.debk007.olamaps.adapter.SearchResultAdapter
import com.debk007.olamaps.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.mapboxsdk.geometry.LatLng
import com.ola.maps.mapslibrary.models.OlaLatLng
import com.ola.maps.mapslibrary.models.OlaMapsConfig
import com.ola.maps.mapslibrary.models.OlaMarkerOptions
import com.ola.maps.navigation.ui.v5.MapStatusCallback
import com.ola.maps.navigation.v5.model.route.RouteInfoData
import com.ola.maps.navigation.v5.navigation.NavigationMapRoute
import com.ola.maps.navigation.v5.navigation.direction.transform

class MainActivity : AppCompatActivity(), MapStatusCallback {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MapsViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLatLng: LatLng
    private var navigationRoute: NavigationMapRoute? = null
    private val directionsRouteList = arrayListOf<DirectionsRoute>()
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            olaMapsInit()
        } else {
            Toast.makeText(this, "The app needs Location permission to work", Toast.LENGTH_SHORT)
                .show()
        }
    }
    private val markerViewOptions = OlaMarkerOptions.Builder()
        .setIconIntRes(R.drawable.ic_location)
        .setMarkerId("1")
        .setIconSize(0.05f)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()

        val searchResultAdapter = SearchResultAdapter { latLng ->
            binding.searchResultRecyclerView.isVisible = false
            binding.directionsBtn.isVisible = true

            binding.olaMapView.apply {
                updateMarkerView(
                    OlaMarkerOptions.Builder()
                        .setMarkerId(markerViewOptions.markerId)
                        .setPosition(
                            OlaLatLng(
                                latitude = latLng.latitude,
                                longitude = latLng.longitude
                            )
                        )
                        .setIconIntRes(markerViewOptions.iconIntRes!!)
                        .setIconSize(markerViewOptions.iconSize)
                        .build()
                )
                navigationRoute?.removeRoute()
                navigationRoute?.animateCamera(latLng, 1.0)
            }

            viewModel.getDirectionsAndAddRoute(
                originLatitudeLongitude = currentLatLng,
                destinationLatitudeLongitude = latLng,
                onSuccess = { routeInfoData ->
                    binding.directionsBtn.setOnClickListener {
                        setupRoute(routeInfoData, latLng)
                        binding.directionsBtn.isVisible = false
                    }
                }
            )

            binding.searchBar.text?.clear()
        }
        binding.searchResultRecyclerView.adapter = searchResultAdapter

        binding.searchBar.setOnEditorActionListener { textView, i, keyEvent ->

            viewModel.getAutoCompleteSearchResults(input = textView.text.toString(),
                onSuccess = { predictionsList ->
                    searchResultAdapter.updateData(predictionsList)
                    binding.searchResultRecyclerView.isVisible = predictionsList.isNotEmpty()
                })

            true
        }

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady() {
        navigationRoute = binding.olaMapView.getNavigationMapRoute()

        binding.olaMapView.addOnMapClickListener {

            true
        }

        binding.myLocationFab.setOnClickListener {
            binding.olaMapView.moveToCurrentLocation()
        }

    }

    override fun onMapLoadFailed(p0: String?) {
        Log.d("ola", "map loading failed: $p0")

        p0?.let {
            val errorCode = p0.substring(it.lastIndex - 2)
            if (errorCode == "401") {
                viewModel.getAccessToken(
                    clientId = BuildConfig.CLIENT_ID,
                    clientSecret = BuildConfig.CLIENT_SECRET,
                    onSuccess = { olaMapsInit() },
                    onFailure = { errorMsg ->
                        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                )
            }

        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            olaMapsInit()

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        currentLatLng = LatLng(it.latitude, it.longitude)

                        binding.olaMapView.addHuddleMarkerView(
                            olaLatLng = OlaLatLng(
                                latitude = currentLatLng.latitude,
                                longitude = currentLatLng.longitude
                            ),
                            headerText = "Current Location",
                            descriptionText = "This is your location"
                        )

                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "unable to fetch current latitude, longitude",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        } else {
            locationPermissionRequest.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun olaMapsInit() {
        binding.olaMapView.initialize(
            mapStatusCallback = this,
            olaMapsConfig = OlaMapsConfig.Builder()
                .setApplicationContext(applicationContext)
                .setClientId(BuildConfig.CLIENT_ID)
                .setMapBaseUrl("https://api.olamaps.io")
                .setInterceptor { chain ->
                    val originalRequest = chain.request()

                    val newRequest = originalRequest.newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer ${viewModel.accessToken}"
                        )
                        .build()

                    chain.proceed(newRequest)
                }
                .setMinZoomLevel(10.0)
                .setMaxZoomLevel(16.0)
                .setZoomLevel(14.0)
                .build()
        )
    }

    private fun setupRoute(routeInfoData: RouteInfoData, latLng: LatLng) {
        navigationRoute?.removeRoute()
        directionsRouteList.clear()
        directionsRouteList.add(transform(routeInfoData))

        navigationRoute?.addRoutesForRoutePreview(directionsRouteList)

        binding.olaMapView.animateCameraWithLatLngs(
            olaLatLngs = listOf(
                OlaLatLng(currentLatLng.latitude, currentLatLng.longitude),
                OlaLatLng(latLng.latitude, latLng.longitude)
            ),
            paddingLeft = 160,
            paddingBottom = 160,
            paddingRight = 160,
            paddingTop = 160,
        )

        binding.olaMapView.removeMarkerViewWithId(markerViewOptions.markerId)
    }

    override fun onStart() {
        super.onStart()
        binding.olaMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.olaMapView.onStop()
    }

    override fun onDestroy() {
        binding.olaMapView.onDestroy()
        super.onDestroy()
    }

}