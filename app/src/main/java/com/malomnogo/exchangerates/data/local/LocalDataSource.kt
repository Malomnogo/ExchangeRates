package com.malomnogo.exchangerates.data.local

import com.malomnogo.exchangerates.data.local.db.entities.Currency
import com.malomnogo.exchangerates.data.local.db.entities.CurrencyEntity
import com.malomnogo.exchangerates.data.local.db.entities.CurrencyDataEntity
import java.util.concurrent.CancellationException
import javax.inject.Inject
import com.malomnogo.exchangerates.data.Result
import com.malomnogo.exchangerates.data.local.db.CurrenciesDao

interface LocalDataSource {

    suspend fun insertAll(currencies: List<CurrencyEntity>)

    suspend fun getAllCurrencies(): Result<List<Currency>>

    suspend fun getFavoriteCurrencies(): Result<List<Currency>>

    suspend fun changeFavoriteCurrency(
        symbol: String,
        isFavourite: Boolean
    ): Result<Currency?>

    suspend fun getMetadataBySymbol(symbol: String): Result<CurrencyDataEntity?>


    class Base @Inject constructor(private val currenciesDao: CurrenciesDao) :
        LocalDataSource {

        override suspend fun insertAll(currencies: List<CurrencyEntity>) {
            currenciesDao.insertAll(currencies)
        }

        override suspend fun getAllCurrencies(): Result<List<Currency>> =
            try {
                Result.success(currenciesDao.getAllCurrencies())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                Result.error(e.message)
            }

        override suspend fun getFavoriteCurrencies(): Result<List<Currency>> =
            try {
                Result.success(currenciesDao.getFavouriteCurrencies())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                Result.error(e.message)
            }

        override suspend fun changeFavoriteCurrency(
            symbol: String,
            isFavourite: Boolean
        ): Result<Currency?> = try {
            val metadata = currenciesDao.getMetadataBySymbol(symbol)
            val newMetadata = metadata?.copy(isFavorite = isFavourite)
                ?: CurrencyDataEntity(symbol = symbol, isFavorite = isFavourite)
            currenciesDao.insertMetadata(newMetadata)
            Result.success(currenciesDao.getCurrencyBySymbol(symbol))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Result.error(e.message)
        }

        override suspend fun getMetadataBySymbol(symbol: String): Result<CurrencyDataEntity?> =
            try {
                Result.success(currenciesDao.getMetadataBySymbol(symbol))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                Result.error(e.message)
            }

    }
}
