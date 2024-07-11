package com.debk007.olamaps

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.debk007.olamaps.databinding.ActivityMainBinding
import com.ola.maps.mapslibrary.models.OlaMapsConfig
import com.ola.maps.mapslibrary.utils.MapTileStyle
import com.ola.maps.navigation.ui.v5.MapStatusCallback

class MainActivity : AppCompatActivity(), MapStatusCallback {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MapsViewModel by viewModels()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLocationPermission()

    }

    override fun onMapReady() {
        Log.d("ola", "map is ready")
    }

    override fun onMapLoadFailed(p0: String?) {
        Log.d("ola", "map loading failed: $p0")

        p0?.let {
            val errorCode = p0.substring(it.lastIndex - 2)
            if (errorCode == "401") {
                viewModel.getAccessToken(
                    clientId = BuildConfig.CLIENT_ID,
                    clientSecret = BuildConfig.CLIENT_SECRET,
                    onSuccess = { olaMapsInit() }
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
        } else {
            locationPermissionRequest.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun olaMapsInit() {
        binding.olaMapView.initialize(
            mapStatusCallback = this,
            olaMapsConfig = OlaMapsConfig.Builder()
                .setApplicationContext(applicationContext) //pass the application context here, it is mandatory
                .setClientId(BuildConfig.CLIENT_ID) //pass the Organization ID here, it is mandatory
                .setMapBaseUrl("https://api.olamaps.io") // pass the Base URL of Ola-Maps here (Stage/Prod URL), it is mandatory
                .setInterceptor { chain ->
                    val originalRequest = chain.request()

                    val newRequest = originalRequest.newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer ${viewModel.accessToken}"
                        )
                        .build()

                    chain.proceed(newRequest)
                } // Instance of okhttp3.Interceptor for with Bearer access token, it is mandatory
                .setMapTileStyle(MapTileStyle.DEFAULT_EARTH_FULL) //pass the MapTileStyle here, it is Optional.
                .setMinZoomLevel(3.0)
                .setMaxZoomLevel(21.0)
                .setZoomLevel(14.0)
                .build()
        )
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