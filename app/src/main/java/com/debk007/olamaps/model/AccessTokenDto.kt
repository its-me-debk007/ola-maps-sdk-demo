package com.debk007.olamaps.model


import com.google.gson.annotations.SerializedName

data class AccessTokenDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("id_token")
    val idToken: String,
    @SerializedName("not-before-policy")
    val notBeforePolicy: Int,
    @SerializedName("refresh_expires_in")
    val refreshExpiresIn: Int,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("token_type")
    val tokenType: String
)