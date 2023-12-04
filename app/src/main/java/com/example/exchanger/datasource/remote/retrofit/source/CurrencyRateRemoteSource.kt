package com.example.exchanger.datasource.remote.retrofit.source

import com.example.exchanger.repository.model.RemoteCurrencyRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyRateRemoteSource {

    @GET(value = "tasks/api/currency-exchange-rates")
    suspend fun getConfiguration(): Response<RemoteCurrencyRateResponse>
}