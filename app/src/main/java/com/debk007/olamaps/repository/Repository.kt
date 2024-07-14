package com.debk007.olamaps.repository

import com.debk007.olamaps.model.AccessTokenDto
import com.debk007.olamaps.model.autocomplete.AutoCompleteResp
import com.debk007.olamaps.util.ApiState
import com.ola.maps.navigation.v5.model.route.RouteInfoData

interface Repository {
    suspend fun getAccessToken(clientId: String, clientSecret: String): ApiState<AccessTokenDto>

    suspend fun getDirections(
        originLatitudeLongitude: Pair<Double, Double>,
        destinationLatitudeLongitude: Pair<Double, Double>
    ): ApiState<RouteInfoData>

    suspend fun getAutoCompleteSearchResults(input: String): ApiState<AutoCompleteResp>
}