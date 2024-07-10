package com.debk007.olamaps.network

import com.debk007.olamaps.model.ProductDetailsDto
import retrofit2.http.GET

interface ApiService {

    @GET("products/1") // TODO: Set API Endpoint
    suspend fun getProductDetails(): ProductDetailsDto // TODO: Set API Response
}