package com.malomnogo.exchangerates.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CurrencyEntity.TABLE_NAME)
data class CurrencyEntity(

    @PrimaryKey
    @ColumnInfo(name = SYMBOL)
    val symbol: String,

    @ColumnInfo(name = RATE)
    val rate: Double,
) {
    companion object {
        const val TABLE_NAME = "currencies"
        const val SYMBOL = "symbol"
        const val RATE = "rate"
    }
}