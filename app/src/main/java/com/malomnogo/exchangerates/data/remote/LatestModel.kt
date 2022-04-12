package com.malomnogo.exchangerates.data.remote

import com.google.gson.annotations.SerializedName

data class LatestServerModel(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Int,
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: List<Rate>
)

data class Rate(
    val symbol: String,
    val rate: Double
)