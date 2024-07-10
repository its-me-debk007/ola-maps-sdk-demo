package com.debk007.olamaps

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.debk007.olamaps.databinding.ActivityMainBinding
import com.ola.maps.mapslibrary.models.OlaMapsConfig
import com.ola.maps.mapslibrary.utils.MapTileStyle
import com.ola.maps.navigation.ui.v5.MapStatusCallback

class MainActivity : AppCompatActivity(), MapStatusCallback {

    private lateinit var binding: ActivityMainBinding
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            olaMapsInit()
        } else {
            Toast.makeText(this, "The app needs Location permission to work", Toast.LENGTH_SHORT)
                .show()
            this.finishAffinity()
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
                .setClientId("3ade56fe-cbcc-4b52-b3ef-79bb5c4bdcc6") //pass the Organization ID here, it is mandatory
                .setMapBaseUrl("https://api.olamaps.io") // pass the Base URL of Ola-Maps here (Stage/Prod URL), it is mandatory
                .setInterceptor { chain ->
                    val originalRequest = chain.request()

                    val newRequest = originalRequest.newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer yJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJMRndtX0U2akoyWG5yYkpkS1d1VXl2UllUN25lZ0FibDhWLXVSTno3UzZVIn0.eyJleHAiOjE3MjA2NTE3MDAsImlhdCI6MTcyMDY0ODEwMCwianRpIjoiOGE3YzU2ZmItYTQyMy00MmUyLWIxOWUtNTAwYWNiNDI4NDJjIiwiaXNzIjoiaHR0cHM6Ly9hY2NvdW50Lm9sYW1hcHMuaW8vcmVhbG1zL29sYW1hcHMiLCJzdWIiOiJmNDU4NDM0ZS1kMDRmLTQ4YmMtOGEzYy0xZDAzODU3YjU2MjIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiIzYWRlNTZmZS1jYmNjLTRiNTItYjNlZi03OWJiNWM0YmRjYzYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtb2xhbWFwcyIsIk9SRy10U2pBQWd5TmlkIiwiU0JOLTUxMDkzYWRkLWNkMDUtNDUyNC05MTBmLTk2OTBhNThhMmJjMiJdfSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY2xpZW50SG9zdCI6IjEwLjM3LjE0Ljg2Iiwib3JnIjoiT1JHLXRTakFBZ3lOaWQiLCJvcmcxIjp7fSwicmVhbG0iOiJvbGFtYXBzIiwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LTNhZGU1NmZlLWNiY2MtNGI1Mi1iM2VmLTc5YmI1YzRiZGNjNiIsImNsaWVudEFkZHJlc3MiOiIxMC4zNy4xNC44NiIsInNibiI6IlNCTi01MTA5M2FkZC1jZDA1LTQ1MjQtOTEwZi05NjkwYTU4YTJiYzIiLCJjbGllbnRfaWQiOiIzYWRlNTZmZS1jYmNjLTRiNTItYjNlZi03OWJiNWM0YmRjYzYifQ.mHKKLaT5svHAwTIcs7uea_V6RTFsa0KMtCexW98wlWQsmyoEdKkvrsGMCpoKJCuk6r_jPSiSqPzvcbRtThiqWJDjQeO1SZSElY1CUzrE_CPKdPbTc4UsZetM0kOaoUC5xUW6ZUlXBDIIJlt98x7trd4NnoeBa0mEu2sYjE7EWw-eyGTWzaT2NtVu0QeKZwBRGGCeLnLb5niYsaZs1n90Gxml-CKrf5gRKUTYGM6lt_r90NUlTq0IOGXj63JRraZ2YMy03_Q5qq12mdTnthKvawi5ZCEhWlIcWB3yky0K28zvD9WsphmKYAyzn4s_Pyb75AdK0XscKd6qnaHQAyoPTQ"
                        )
                        .build()

                    chain.proceed(newRequest)
                } // Instance of okhttp3.Interceptor for with Bearer access token, it is mandatory
                .setMapTileStyle(MapTileStyle.BOLT_DARK) //pass the MapTileStyle here, it is Optional.
                .setMinZoomLevel(3.0)
                .setMaxZoomLevel(21.0)
                .setZoomLevel(18.0)
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