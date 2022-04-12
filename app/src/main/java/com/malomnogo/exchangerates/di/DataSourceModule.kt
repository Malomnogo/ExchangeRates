package com.malomnogo.exchangerates.di

import com.malomnogo.exchangerates.data.local.db.CurrenciesDao
import com.malomnogo.exchangerates.data.local.LocalDataSource
import com.malomnogo.exchangerates.data.remote.LatestService
import com.malomnogo.exchangerates.data.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        service: LatestService
    ): RemoteDataSource = RemoteDataSource.Base(service)

    @Provides
    @Singleton
    fun provideLocalDataSource(
        dao: CurrenciesDao
    ): LocalDataSource = LocalDataSource.Base(dao)

}