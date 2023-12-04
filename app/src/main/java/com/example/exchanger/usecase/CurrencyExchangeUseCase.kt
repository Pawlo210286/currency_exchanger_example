package com.example.exchanger.usecase

import com.example.exchanger.data.domain.CurrencyExchangeResult
import com.example.exchanger.data.domain.CurrencyExchangeResult.Error
import com.example.exchanger.data.domain.CurrencyRate
import com.example.exchanger.data.domain.Wallet
import com.example.exchanger.datasource.local.ProfileStateProvider
import com.example.exchanger.repository.CurrencyExchangerRepository
import com.example.transfer.extension.transform
import com.example.transfer.model.Transfer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CurrencyExchangeUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val repository: CurrencyExchangerRepository,
    private val profileStateProvider: ProfileStateProvider
) {

    suspend operator fun invoke(
        amount: Double,
        currentCurrencyId: String,
        targetCurrencyId: String,
    ): Transfer<CurrencyExchangeResult> = withContext(dispatcher) {
        repository.loadCurrencyRates().transform { currencyRateData ->
            val profileState = profileStateProvider.requiredProfileState

            val currentCurrencyRate = currencyRateData.rates.firstOrNull {
                it.currencyId == currentCurrencyId
            } ?: return@transform Error

            val baseCurrencyRate =
                currencyRateData.rates.firstOrNull { it.currencyId == currencyRateData.baseCurrencyId }
                    ?: return@transform Error

            val targetCurrencyRate =
                currencyRateData.rates.firstOrNull { it.currencyId == targetCurrencyId }
                    ?: return@transform Error

            val rawConvertedCurrencyAmount = getRawConvertedAmount(
                amount = amount,
                currency = currentCurrencyRate,
                baseCurrencyRate = baseCurrencyRate,
                targetCurrencyRate = targetCurrencyRate
            )

            val commissionFeeRate = getCommissionFeeRate(profileState.transactionCount)
            val commissionFee = rawConvertedCurrencyAmount * commissionFeeRate
            val convertedCurrencyAmount = rawConvertedCurrencyAmount - commissionFee

            val wallets = profileState.wallets.toMutableList()
            val baseWallet = wallets.firstOrNull {
                it.currencyId == currentCurrencyId && it.balance >= amount
            } ?: return@transform Error

            val targetWallet = wallets.firstOrNull {
                it.currencyId == targetCurrencyId
            } ?: Wallet(
                currencyId = targetCurrencyId,
                balance = 0.0
            )

            val updatedBaseWallet = baseWallet.copy(
                balance = baseWallet.balance - amount
            )

            val updatedTargetWallet = targetWallet.copy(
                balance = targetWallet.balance + convertedCurrencyAmount
            )

            if (updatedBaseWallet.balance < 0) {
                return@transform Error
            }

            if (wallets.firstOrNull { it.currencyId == targetCurrencyId } == null) {
                wallets.add(targetWallet)
            }

            wallets.replaceAll {
                when (it.currencyId) {
                    currentCurrencyId -> updatedBaseWallet
                    targetCurrencyId -> updatedTargetWallet
                    else -> it
                }
            }

            profileStateProvider.updateProfile(
                newProfileState = profileState.copy(
                    wallets = wallets,
                    transactionCount = profileState.transactionCount + 1
                )
            )

            CurrencyExchangeResult.Success(
                baseCurrencyId = currentCurrencyId,
                targetCurrencyId = targetCurrencyId,
                baseAmountTaken = amount,
                targetAmountReceived = convertedCurrencyAmount,
                commissionFee = commissionFee,
            )
        }
    }

    private fun getCommissionFeeRate(
        transactionCount: Int,
    ): Double = if (transactionCount < COMMISSION_FREE_OPERATIONS_LIMIT) {
        0.0
    } else {
        COMMISSION_FEE_RATE
    }

    private fun getRawConvertedAmount(
        amount: Double,
        currency: CurrencyRate,
        baseCurrencyRate: CurrencyRate,
        targetCurrencyRate: CurrencyRate,
    ): Double =
        if (currency.currencyId == baseCurrencyRate.currencyId) {
            amount * targetCurrencyRate.value
        } else {
            (amount * baseCurrencyRate.value) / targetCurrencyRate.value
        }

    companion object {
        private const val COMMISSION_FREE_OPERATIONS_LIMIT = 5
        private const val COMMISSION_FEE_RATE = 0.07
    }
}