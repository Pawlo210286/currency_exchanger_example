package com.example.exchanger.usecase

import com.example.exchanger.data.domain.CurrencyRateData
import com.example.exchanger.repository.CurrencyExchangerRepository
import com.example.transfer.model.Transfer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetCurrencyRateUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val repository: CurrencyExchangerRepository,
) {

    suspend operator fun invoke(): Transfer<CurrencyRateData> = withContext(dispatcher) {
        repository.loadCurrencyRates()
    }
}
