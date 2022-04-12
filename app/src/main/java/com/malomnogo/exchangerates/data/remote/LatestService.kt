package com.malomnogo.exchangerates.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface LatestService {

    @GET("latest")
    suspend fun getLatestCurrencies(
        @Query("base") base: String? = null
    ): LatestServerModel

}