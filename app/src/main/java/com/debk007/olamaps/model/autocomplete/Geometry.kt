package com.debk007.olamaps.model.autocomplete


import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("location")
    val location: Location
)