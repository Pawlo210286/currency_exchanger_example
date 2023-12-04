package com.example.exchanger.repository

import com.example.exchanger.data.domain.CurrencyRate
import com.example.exchanger.data.domain.CurrencyRateData
import com.example.exchanger.data.domain.TransactionType
import com.example.transfer.model.Transfer

interface CurrencyExchangerRepository {

    suspend fun loadCurrencyRates(): Transfer<CurrencyRateData>

}
