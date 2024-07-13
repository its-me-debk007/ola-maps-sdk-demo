package com.debk007.olamaps.repository

import com.debk007.olamaps.model.AccessTokenDto
import com.debk007.olamaps.network.ApiService
import com.debk007.olamaps.util.ApiState
import org.json.JSONObject

class RepositoryImpl(
    private val apiService: ApiService
) : Repository {

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String
    ): ApiState<AccessTokenDto> = try {
        val response = apiService.getAccessToken(clientId, clientSecret)

        if (response.isSuccessful) {
            ApiState.Success(response.body()!!)
        } else  {
            response.errorBody()!!.charStream().use { reader ->
                val jsonObj = JSONObject(reader.readText())
                throw Exception(jsonObj.toString())
            }

        }
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }
}