package com.debk007.olamaps.repository

import com.debk007.olamaps.model.AccessTokenDto
import com.debk007.olamaps.model.autocomplete.AutoCompleteResp
import com.debk007.olamaps.util.ApiState
import com.mapbox.mapboxsdk.geometry.LatLng
import com.ola.maps.navigation.v5.model.route.RouteInfoData

interface Repository {
    suspend fun getAccessToken(clientId: String, clientSecret: String): ApiState<AccessTokenDto>

    suspend fun getAutoCompleteSearchResults(input: String): ApiState<AutoCompleteResp>

    suspend fun getRouteInfo(
        originLatLng: LatLng,
        destinationLatLng: LatLng
    ): ApiState<RouteInfoData>
}