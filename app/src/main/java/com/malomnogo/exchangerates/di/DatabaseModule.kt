package com.malomnogo.exchangerates.di

import android.content.Context
import androidx.room.Room
import com.malomnogo.exchangerates.data.local.db.CurrenciesDb
import com.malomnogo.exchangerates.data.local.db.CurrenciesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): CurrenciesDb {
        return Room.databaseBuilder(
            appContext,
            CurrenciesDb::class.java,
            "currencies.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: CurrenciesDb): CurrenciesDao {
        return appDatabase.currenciesDao()
    }
}