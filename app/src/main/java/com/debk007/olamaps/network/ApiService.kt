package com.debk007.olamaps.network

import com.debk007.olamaps.BuildConfig
import com.debk007.olamaps.model.AccessTokenDto
import com.debk007.olamaps.model.autocomplete.AutoCompleteResp
import com.ola.maps.navigation.v5.model.route.RouteInfoData
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

object AccessRetrofitClient {
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://account.olamaps.io/realms/olamaps/protocol/openid-connect/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val accessApiService = retrofitBuilder.create(AccessApiService::class.java)
}

object RetrofitClient {
    private val okhttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()

            val originalUrl = originalRequest.url

            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
        .build()

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://api.olamaps.io/")
        .client(okhttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofitBuilder.create(ApiService::class.java)
}

interface AccessApiService {

    @FormUrlEncoded
    @POST("token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("scope") scope: String = "openid",
    ): Response<AccessTokenDto>
}

interface ApiService {

    @POST("routing/v1/directions")
    suspend fun getRouteInfo(
        @QueryMap queryMap: Map<String, String>
    ): Response<RouteInfoData>

    @GET("places/v1/autocomplete")
    suspend fun getAutoCompleteSearchResults(
        @Query("input") input: String
    ): Response<AutoCompleteResp>
}