package com.debk007.olamaps.repository

import com.debk007.olamaps.model.AccessTokenDto
import com.debk007.olamaps.model.autocomplete.AutoCompleteResp
import com.debk007.olamaps.network.AccessApiService
import com.debk007.olamaps.network.ApiService
import com.debk007.olamaps.util.ApiState
import com.mapbox.mapboxsdk.geometry.LatLng
import com.ola.maps.navigation.v5.model.route.RouteInfoData
import org.json.JSONObject

class RepositoryImpl(
    private val accessApiService: AccessApiService,
    private val apiService: ApiService
) : Repository {

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String
    ): ApiState<AccessTokenDto> = try {
        val response = accessApiService.getAccessToken(clientId, clientSecret)

        if (response.isSuccessful) {
            ApiState.Success(response.body()!!)
        } else {
            response.errorBody()!!.charStream().use { reader ->
                val jsonObj = JSONObject(reader.readText())
                throw Exception(jsonObj.toString())
            }

        }
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }

    override suspend fun getRouteInfo(
        originLatLng: LatLng,
        destinationLatLng: LatLng,
    ): ApiState<RouteInfoData> = try {
        val queryMap = mapOf(
            "origin" to "${originLatLng.latitude},${originLatLng.longitude}",
            "destination" to "${destinationLatLng.latitude},${destinationLatLng.longitude}",
            "alternatives" to "false",
            "steps" to "true",
            "overview" to "full",
            "language" to "en",
            "traffic_metadata" to "false",
        )

        val response = apiService.getRouteInfo(queryMap)

        if (response.isSuccessful) {
            ApiState.Success(response.body()!!)
        } else {
            response.errorBody()!!.charStream().use { reader ->
                val jsonObj = JSONObject(reader.readText())
                throw Exception(jsonObj.toString())
            }

        }
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }

    override suspend fun getAutoCompleteSearchResults(
        input: String
    ): ApiState<AutoCompleteResp> = try {
        val response = apiService.getAutoCompleteSearchResults(input)

        if (response.isSuccessful) {
            ApiState.Success(response.body()!!)
        } else {
            response.errorBody()!!.charStream().use { reader ->
                val jsonObj = JSONObject(reader.readText())
                throw Exception(jsonObj.toString())
            }

        }
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }
}