package com.example.exchanger.repository

import com.example.exchanger.data.domain.CurrencyRateData
import com.example.exchanger.datasource.remote.retrofit.source.CurrencyRateRemoteSource
import com.example.exchanger.repository.mapper.toDomain
import com.example.transfer.extension.fromAsyncResponse
import com.example.transfer.extension.transform
import com.example.transfer.model.Transfer

class CurrencyExchangerRepositoryImpl(
    private val remoteSource: CurrencyRateRemoteSource
) : CurrencyExchangerRepository {

    override suspend fun loadCurrencyRates(): Transfer<CurrencyRateData> =
        Transfer.fromAsyncResponse {
            remoteSource.getConfiguration()
        }.transform {
            it.toDomain()
        }
}
