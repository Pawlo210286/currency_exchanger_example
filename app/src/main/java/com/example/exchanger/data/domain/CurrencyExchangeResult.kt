package com.example.exchanger.data.domain

sealed class CurrencyExchangeResult {
    data class Success(
        val baseCurrencyId: String,
        val targetCurrencyId: String,
        val baseAmountTaken: Double,
        val targetAmountReceived: Double,
        val commissionFee: Double,
    ) : CurrencyExchangeResult()

    data object Error : CurrencyExchangeResult()
}
