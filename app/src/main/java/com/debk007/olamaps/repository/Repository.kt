package com.debk007.olamaps.repository

import com.debk007.olamaps.model.ProductDetailsDto
import com.debk007.olamaps.util.ApiState

interface Repository {
    suspend fun getAccessToken(): ApiState<ProductDetailsDto>
}