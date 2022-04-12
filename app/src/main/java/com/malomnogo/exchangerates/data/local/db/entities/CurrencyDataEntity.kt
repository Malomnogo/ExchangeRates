package com.malomnogo.exchangerates.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = CurrencyDataEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = [CurrencyEntity.SYMBOL],
            childColumns = [CurrencyDataEntity.SYMBOL]
        )
    ]
)
data class CurrencyDataEntity(

    @PrimaryKey
    @ColumnInfo(name = SYMBOL)
    val symbol: String,

    @ColumnInfo(name = IS_FAVORITE)
    val isFavorite: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "currency_metadata"
        const val SYMBOL = "symbol"
        const val IS_FAVORITE = "is_favorite"
    }
}