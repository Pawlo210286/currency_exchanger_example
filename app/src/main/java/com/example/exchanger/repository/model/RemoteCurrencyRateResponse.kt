package com.example.exchanger.repository.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteCurrencyRateResponse(
    @field:Json(name = "base") val baseCurrencyId: String,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "rates") val ratesHolder: RemoteCurrencyRatesHolder,
)
