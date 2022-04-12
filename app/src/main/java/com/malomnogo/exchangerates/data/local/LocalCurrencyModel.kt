package com.malomnogo.exchangerates.data.local

data class LocalCurrencyModel(
    val symbol: String,
    val rate: Double,
    val isFavorite: Boolean
)