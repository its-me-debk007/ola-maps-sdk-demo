package com.debk007.olamaps.network

import com.debk007.olamaps.model.AccessTokenDto
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

object RetrofitClient {
    private val retrofitBuilder = Retrofit.Builder()
    .baseUrl("https://account.olamaps.io/realms/olamaps/protocol/openid-connect/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

    val apiService = retrofitBuilder.create(ApiService::class.java)
}

interface ApiService {

    @FormUrlEncoded
    @POST("token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("scope") scope: String = "openid",
    ): Response<AccessTokenDto>
}