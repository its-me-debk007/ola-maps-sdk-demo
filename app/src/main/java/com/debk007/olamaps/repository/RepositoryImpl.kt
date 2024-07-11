package com.debk007.olamaps.repository

import com.debk007.olamaps.model.AccessTokenDto
import com.debk007.olamaps.network.ApiService
import com.debk007.olamaps.util.ApiState

class RepositoryImpl(
    private val apiService: ApiService
) : Repository {

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String
    ): ApiState<AccessTokenDto> = try {
        ApiState.Success(apiService.getAccessToken(clientId, clientSecret))
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }
}