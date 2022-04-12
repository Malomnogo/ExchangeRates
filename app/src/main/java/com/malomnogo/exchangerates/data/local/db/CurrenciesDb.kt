package com.malomnogo.exchangerates.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.malomnogo.exchangerates.data.local.db.entities.CurrencyEntity
import com.malomnogo.exchangerates.data.local.db.entities.CurrencyDataEntity

@Database(
    entities = [CurrencyEntity::class, CurrencyDataEntity::class],
    version = 1,
    exportSchema = true
)
abstract class CurrenciesDb : RoomDatabase() {

    abstract fun currenciesDao(): CurrenciesDao
}