package com.example.exchanger.data.domain

data class CurrencyRateData(
    val baseCurrencyId: String,
    val date: String,
    val rates: List<CurrencyRate>
)
