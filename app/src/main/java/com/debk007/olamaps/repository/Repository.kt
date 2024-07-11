package com.debk007.olamaps.repository

import com.debk007.olamaps.util.ApiState
import com.debk007.olamaps.model.AccessTokenDto

interface Repository {
    suspend fun getAccessToken(clientId: String, clientSecret: String): ApiState<AccessTokenDto>
}