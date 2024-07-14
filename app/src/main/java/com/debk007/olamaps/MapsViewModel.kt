package com.debk007.olamaps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debk007.olamaps.model.autocomplete.Prediction
import com.debk007.olamaps.network.AccessRetrofitClient
import com.debk007.olamaps.network.RetrofitClient
import com.debk007.olamaps.repository.RepositoryImpl
import com.debk007.olamaps.util.ApiState
import com.mapbox.mapboxsdk.geometry.LatLng
import com.ola.maps.navigation.v5.model.route.RouteInfoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsViewModel : ViewModel() {

    private val repository = RepositoryImpl(
        accessApiService = AccessRetrofitClient.accessApiService,
        apiService = RetrofitClient.apiService
    )

    // creating bool variable since viewModel.getAccessToken() can be called multiple times
    private var isApiCalled = false

    var accessToken = ""
    fun getAccessToken(
        clientId: String,
        clientSecret: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (isApiCalled) return

        isApiCalled = true

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAccessToken(clientId, clientSecret)

            if (result is ApiState.Success) {
                Log.d("ola", "Access Token: ${result.data.accessToken}")
                accessToken = result.data.accessToken

                withContext(Dispatchers.Main) {
                    onSuccess()
                }

            } else if (result is ApiState.Error) {
                Log.d("ola", "ERROR: ${result.errorMsg}")

                withContext(Dispatchers.Main) {
                    onFailure(result.errorMsg)
                }
            }
        }
    }

    fun getRouteInfo(
        originLatitudeLongitude: LatLng,
        destinationLatitudeLongitude: LatLng,
        onSuccess: (RouteInfoData) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getRouteInfo(
                originLatLng = originLatitudeLongitude,
                destinationLatLng = destinationLatitudeLongitude
            )

            if (result is ApiState.Success) {
                withContext(Dispatchers.Main) {
                    onSuccess(result.data)
                }

            } else if (result is ApiState.Error) {
                Log.d("ola", "ERROR: ${result.errorMsg}")
            }
        }
    }

    fun getAutoCompleteSearchResults(input: String, onSuccess: (List<Prediction>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAutoCompleteSearchResults(input)

            if (result is ApiState.Success) {
                withContext(Dispatchers.Main) {
                    onSuccess(result.data.predictions)
                }

            } else if (result is ApiState.Error) {
                Log.d("ola", "ERROR: ${result.errorMsg}")
            }
        }
    }
}