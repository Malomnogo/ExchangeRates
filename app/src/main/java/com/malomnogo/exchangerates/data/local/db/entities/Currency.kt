package com.malomnogo.exchangerates.data.local.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class Currency(

    @Embedded
    val currency: CurrencyEntity,

    @Relation(parentColumn = CurrencyEntity.SYMBOL, entityColumn = CurrencyDataEntity.SYMBOL)
    val data: CurrencyDataEntity?
)