package com.malomnogo.exchangerates.data

import com.malomnogo.exchangerates.data.local.LocalCurrencyModel
import com.malomnogo.exchangerates.data.local.LocalDataSource
import com.malomnogo.exchangerates.data.local.db.entities.Currency
import com.malomnogo.exchangerates.data.mappers.toEntity
import com.malomnogo.exchangerates.data.mappers.toLocalModel
import com.malomnogo.exchangerates.data.remote.RemoteDataSource
import javax.inject.Inject

interface CurrenciesRepository {

    suspend fun getCurrencyList(
        base: String?,
        forceRefresh: Boolean = true
    ): Result<List<LocalCurrencyModel>>

    suspend fun getFavoriteCurrencyList(): Result<List<Currency>>

    suspend fun changeFavoriteCurrency(
        symbol: String,
        isFavorite: Boolean
    ): Result<Currency?>

    class Base @Inject constructor(
        private val remoteDataSource: RemoteDataSource,
        private val localDataSource: LocalDataSource
    ) : CurrenciesRepository {

        override suspend fun getCurrencyList(
            base: String?,
            forceRefresh: Boolean
        ): Result<List<LocalCurrencyModel>> {
            if (forceRefresh) {
                val result = remoteDataSource.fetchLatestCurrencies(base)
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        val entities = result.data?.map { it.toEntity() }
                        localDataSource.insertAll(entities!!)
                    }
                    Result.Status.ERROR -> {
                        return Result.error(result.message)
                    }
                }
            }
            val result = localDataSource.getAllCurrencies()
            return when (result.status) {
                Result.Status.SUCCESS -> {
                    val entities = result.data?.map { it.toLocalModel() }
                    Result.success(entities)
                }
                Result.Status.ERROR -> {
                    Result.error(result.message)
                }
            }
        }

        override suspend fun getFavoriteCurrencyList(): Result<List<Currency>> =
            localDataSource.getFavoriteCurrencies()

        override suspend fun changeFavoriteCurrency(
            symbol: String,
            isFavorite: Boolean
        ): Result<Currency?> =
            localDataSource.changeFavoriteCurrency(symbol, isFavorite)
    }
}