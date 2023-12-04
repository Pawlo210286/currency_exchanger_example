package com.example.exchanger.repository.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

class RemoteCurrencyRatesJsonAdapter {

    @FromJson
    fun fromJson(jsonReader: JsonReader): RemoteCurrencyRatesHolder = with(jsonReader) {
        val currencyRates = mutableListOf<RemoteCurrencyRate>()
        beginObject()

        while (hasNext()) {
            val currencyId = nextName()
            val currencyValue = nextDouble()

            currencyRates.add(
                RemoteCurrencyRate(
                    currencyId = currencyId,
                    value = currencyValue
                )
            )
        }

        endObject()
        return RemoteCurrencyRatesHolder(currencyRates)
    }
}