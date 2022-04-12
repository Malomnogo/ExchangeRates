package com.malomnogo.exchangerates.data.local.db

import androidx.room.*
import com.malomnogo.exchangerates.data.local.db.entities.Currency
import com.malomnogo.exchangerates.data.local.db.entities.CurrencyEntity
import com.malomnogo.exchangerates.data.local.db.entities.CurrencyDataEntity

@Dao
interface CurrenciesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)

    @Transaction
    @Query("SELECT * FROM ${CurrencyEntity.TABLE_NAME}")
    suspend fun getAllCurrencies(): List<Currency>

    @Transaction
    @Query(
        "SELECT * FROM ${CurrencyEntity.TABLE_NAME} " +
                "INNER JOIN ${CurrencyDataEntity.TABLE_NAME} " +
                "ON ${CurrencyEntity.TABLE_NAME}.${CurrencyEntity.SYMBOL} " +
                "= ${CurrencyDataEntity.TABLE_NAME}.${CurrencyDataEntity.SYMBOL} " +
                "WHERE ${CurrencyDataEntity.IS_FAVORITE}=1"
    )
    suspend fun getFavouriteCurrencies(): List<Currency>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(currencyDataEntity: CurrencyDataEntity): Long

    @Query("SELECT * FROM ${CurrencyDataEntity.TABLE_NAME} WHERE ${CurrencyDataEntity.SYMBOL}=:symbol")
    suspend fun getMetadataBySymbol(symbol: String): CurrencyDataEntity?

    @Transaction
    @Query("SELECT * FROM ${CurrencyEntity.TABLE_NAME} WHERE ${CurrencyEntity.SYMBOL}=:symbol")
    suspend fun getCurrencyBySymbol(symbol: String): Currency?
}