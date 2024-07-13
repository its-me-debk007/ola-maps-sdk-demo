package com.debk007.olamaps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debk007.olamaps.network.RetrofitClient
import com.debk007.olamaps.repository.RepositoryImpl
import com.debk007.olamaps.util.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsViewModel : ViewModel() {

    private val repository = RepositoryImpl(RetrofitClient.apiService)

    // creating bool variable since viewModel.getAccessToken() can be called multiple times
    private var isApiCalled = false

    var accessToken = ""
    fun getAccessToken(clientId: String, clientSecret: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
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
}