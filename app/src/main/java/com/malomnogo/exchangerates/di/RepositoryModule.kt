package com.malomnogo.exchangerates.di

import com.malomnogo.exchangerates.data.CurrenciesRepository
import com.malomnogo.exchangerates.data.local.LocalDataSource
import com.malomnogo.exchangerates.data.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): CurrenciesRepository = CurrenciesRepository.Base(remoteDataSource, localDataSource)

}