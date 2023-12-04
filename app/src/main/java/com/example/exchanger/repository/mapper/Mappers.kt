package com.example.exchanger.repository.mapper

import com.example.exchanger.data.domain.CurrencyRate
import com.example.exchanger.data.domain.CurrencyRateData
import com.example.exchanger.repository.model.RemoteCurrencyRate
import com.example.exchanger.repository.model.RemoteCurrencyRateResponse

internal fun RemoteCurrencyRateResponse.toDomain(): CurrencyRateData =
    CurrencyRateData(
        baseCurrencyId = baseCurrencyId,
        date = date,
        rates = ratesHolder.rates.map {
            it.toDomain()
        }
    )

internal fun RemoteCurrencyRate.toDomain(): CurrencyRate =
    CurrencyRate(
        currencyId = currencyId,
        value = value,
    )