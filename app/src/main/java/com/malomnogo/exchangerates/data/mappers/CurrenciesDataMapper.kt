package com.malomnogo.exchangerates.data.mappers
import com.malomnogo.exchangerates.data.local.db.entities.CurrencyEntity
import com.malomnogo.exchangerates.data.remote.Rate

fun Rate.toEntity() = CurrencyEntity(
    symbol = this.symbol,
    rate = this.rate
)