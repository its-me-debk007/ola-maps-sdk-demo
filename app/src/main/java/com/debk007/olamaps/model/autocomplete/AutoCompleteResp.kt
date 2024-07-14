package com.debk007.olamaps.model.autocomplete


import com.google.gson.annotations.SerializedName

data class AutoCompleteResp(
    @SerializedName("error_message")
    val errorMessage: String,
    @SerializedName("info_messages")
    val infoMessages: List<Any>,
    @SerializedName("predictions")
    val predictions: List<Prediction>,
    @SerializedName("status")
    val status: String
)