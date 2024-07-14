package com.debk007.olamaps.model.autocomplete


import com.google.gson.annotations.SerializedName

data class SecondaryTextMatchedSubstring(
    @SerializedName("length")
    val length: Int,
    @SerializedName("offset")
    val offset: Int
)