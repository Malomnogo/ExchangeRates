package com.malomnogo.exchangerates.di

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malomnogo.exchangerates.data.remote.LatestServerModel
import com.malomnogo.exchangerates.data.remote.LatestService
import com.malomnogo.exchangerates.data.remote.Rate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                val url = request.url.newBuilder()
                    .addQueryParameter("access_key", "c432a0cbb8f32c943459b804b5bbc9aa")
                    .build()
                val resultRequest = request.newBuilder().url(url).build()
                chain.proceed(resultRequest)
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://api.exchangeratesapi.io/v1/")
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideLatestService(retrofit: Retrofit): LatestService {
        return retrofit.create(LatestService::class.java)
    }

    @Provides
    @Singleton
    fun provideGsonConverter(): Converter.Factory = GsonConverterFactory.create(
        GsonBuilder().registerTypeAdapter(LatestServerModel::class.java, CustomDeserializer())
            .create()
    )

    class CustomDeserializer : JsonDeserializer<LatestServerModel> {

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?): LatestServerModel {

            val obj = json!!.asJsonObject
            val success = obj.get("success").asBoolean
            val timestamp = obj.get("timestamp").asInt
            val base = obj.get("base").asString
            val date = obj.get("date").asString

            val rates = obj.get("rates").asJsonObject
            val list = mutableListOf<Rate>()
            val iterator = rates.entrySet().iterator()
            for (mutableEntry in iterator) {
                list.add(Rate(mutableEntry.key, mutableEntry.value.asDouble))
            }
            return LatestServerModel(success, timestamp, base, date, list)
        }
    }
}