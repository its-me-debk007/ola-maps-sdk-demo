package com.debk007.olamaps.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessTokenDto(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "expires_in")
    val expiresIn: Int,
    @Json(name = "id_token")
    val idToken: String,
    @Json(name = "not-before-policy")
    val notBeforePolicy: Int,
    @Json(name = "refresh_expires_in")
    val refreshExpiresIn: Int,
    @Json(name = "scope")
    val scope: String,
    @Json(name = "token_type")
    val tokenType: String
)