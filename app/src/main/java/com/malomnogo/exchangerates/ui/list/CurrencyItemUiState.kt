package com.malomnogo.exchangerates.ui.list

data class CurrencyItemUiState(
    val symbol: String,
    val rate: Double,
    var isFavorite: Boolean,
    val onFavoriteClick: (symbol: String, isFavorite: Boolean) -> Unit
)