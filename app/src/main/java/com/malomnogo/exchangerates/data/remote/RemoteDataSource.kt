package com.malomnogo.exchangerates.data.remote

import java.util.concurrent.CancellationException
import javax.inject.Inject
import com.malomnogo.exchangerates.data.Result

interface RemoteDataSource {

    suspend fun fetchLatestCurrencies(base: String?): Result<List<Rate>>

    class Base @Inject constructor(private val service: LatestService) : RemoteDataSource {

        override suspend fun fetchLatestCurrencies(base: String?): Result<List<Rate>> = try {
            Result.success(service.getLatestCurrencies(base).rates)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Result.error(e.message)
        }
    }

}