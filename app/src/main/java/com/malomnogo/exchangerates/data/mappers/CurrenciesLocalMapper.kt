package com.malomnogo.exchangerates.data.mappers

import com.malomnogo.exchangerates.data.local.LocalCurrencyModel
import com.malomnogo.exchangerates.data.local.db.entities.Currency

fun Currency.toLocalModel() = LocalCurrencyModel(
    symbol = this.currency.symbol,
    rate = this.currency.rate,
    isFavorite = this.data?.isFavorite ?: false
)

