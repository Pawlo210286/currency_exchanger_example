package com.example.exchanger.di

import com.example.exchanger.repository.CurrencyExchangerRepository
import com.example.exchanger.repository.CurrencyExchangerRepositoryImpl
import com.example.exchanger.repository.model.RemoteCurrencyRatesJsonAdapter
import com.example.exchanger.datasource.remote.retrofit.provider.RemoteSourceProvider
import com.example.exchanger.datasource.remote.retrofit.provider.RemoteSourceProviderImpl
import com.example.exchanger.datasource.remote.retrofit.source.CurrencyRateRemoteSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val CURRENCY_REMOTE_SOURCE_PROVIDER = "CURRENCY_REMOTE_SOURCE_PROVIDER"

val networkModule = module {

    single {
        Moshi.Builder()
            .add(RemoteCurrencyRatesJsonAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    single<MoshiConverterFactory> {
        MoshiConverterFactory.create(get())
    }

    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    single {
        val converterFactory: MoshiConverterFactory = get()
        Retrofit.Builder().client(get()).addConverterFactory(converterFactory)
    }

    single<RemoteSourceProvider>(named(CURRENCY_REMOTE_SOURCE_PROVIDER)) {
        RemoteSourceProviderImpl(get())
    }

    factory {
        get<RemoteSourceProvider>(named(CURRENCY_REMOTE_SOURCE_PROVIDER)).create(
            CurrencyRateRemoteSource::class.java
        )
    }

    factory<CurrencyExchangerRepository> { CurrencyExchangerRepositoryImpl(get()) }
}