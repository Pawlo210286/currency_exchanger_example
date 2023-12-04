package com.example.exchanger.datasource.remote.retrofit.provider

import retrofit2.Retrofit

interface RemoteSourceProvider {

    fun <T> create(kClass: Class<out T>): T
}

internal class RemoteSourceProviderImpl(
    private val retrofitBuilder: Retrofit.Builder,
) : RemoteSourceProvider {

    override fun <T> create(kClass: Class<out T>): T =
        retrofitBuilder.baseUrl(BASE_URL).build().create(kClass)

    companion object {
        private const val BASE_URL = "https://developers.paysera.com/"
    }
}
