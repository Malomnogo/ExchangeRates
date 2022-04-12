package com.malomnogo.exchangerates.data.mappers

import com.malomnogo.exchangerates.data.local.LocalCurrencyModel
import com.malomnogo.exchangerates.data.local.db.entities.Currency
import com.malomnogo.exchangerates.ui.list.CurrencyItemUiState

fun LocalCurrencyModel.toUiState(onFavouriteClick: (symbol: String, isFavourite: Boolean) -> Unit) =
    CurrencyItemUiState(
        symbol = this.symbol,
        rate = this.rate,
        isFavorite = this.isFavorite,
        onFavoriteClick = onFavouriteClick
    )

fun Currency.toUiState(onFavouriteClick: (symbol: String, isFavourite: Boolean) -> Unit) =
    CurrencyItemUiState(
        symbol = this.currency.symbol,
        rate = this.currency.rate,
        isFavorite = this.data?.isFavorite ?: false,
        onFavoriteClick = onFavouriteClick
    )