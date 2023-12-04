package com.example.exchanger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.extensions.LiveEvent
import com.example.core.extensions.MutableLiveEvent
import com.example.core.extensions.post
import com.example.exchanger.data.domain.CurrencyExchangeResult
import com.example.exchanger.data.domain.CurrencyRate
import com.example.exchanger.data.domain.ProfileState
import com.example.exchanger.usecase.CurrencyExchangeUseCase
import com.example.exchanger.usecase.GetCurrencyRateUseCase
import com.example.exchanger.usecase.GetProfileStateUseCase
import com.example.exchanger.viewmodel.CurrencyExchangeOverviewViewModel.State.Loading
import com.example.transfer.extension.getOrElse
import com.example.transfer.extension.unfold
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CurrencyExchangeOverviewViewModel(
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private val getProfileStateUseCase: GetProfileStateUseCase,
    private val currencyExchangeUseCase: CurrencyExchangeUseCase,
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext get() = viewModelScope.coroutineContext

    private val mutableState: MutableStateFlow<State> = MutableStateFlow(Loading)
    val state: StateFlow<State> = mutableState

    private val mutableEvent: MutableLiveEvent<Event> = MutableLiveEvent()
    val event: LiveEvent<Event> = mutableEvent

    private var syncRateJob: Job? = null

    sealed class State {
        data object Loading : State()
        data object Error : State()
        data class Content(
            val profileState: ProfileState,
            val currencyRates: List<CurrencyRate>,
            val receiveExchangeAmount: Double,
            val sellExchangeAmount: Double,
        ) : State()
    }

    sealed class Event {
        data object CurrencyRateUpdatingError : Event()
        data class CurrencyExchangeTransactionResult(val result: CurrencyExchangeResult) : Event()
    }

    init {
        launch {
            mutableState.value = Loading
            getProfileStateUseCase().collectLatest {
                val currentState = state.value
                if (currentState is State.Content) {
                    mutableState.value = currentState.copy(
                        profileState = it
                    )
                } else {
                    mutableState.value = State.Content(
                        profileState = it,
                        currencyRates = emptyList(),
                        receiveExchangeAmount = 0.0,
                        sellExchangeAmount = 0.0,
                    )
                }
            }
        }
    }

    fun launchRateSync() {
        launch {
            mutableState.value = Loading
            stopRateSync()
            syncRateJob = async {
                while (true) {
                    loadCurrencyRate()
                    delay(RATE_SYNC_PERIOD_MILLIS)
                }
            }
        }

    }

    fun stopRateSync() {
        syncRateJob?.cancel()
    }

    private fun loadCurrencyRate() {
        launch {
            getCurrencyRateUseCase().unfold(
                success = {
                    val currentState = state.value
                    if (currentState is State.Content) {
                        mutableState.value = currentState.copy(
                            currencyRates = it.rates
                        )
                    }
                },
                failure = {
                    mutableEvent.post(Event.CurrencyRateUpdatingError)
                }
            )
        }
    }

    fun exchangeCurrency(
        amount: Double,
        currentCurrencyId: String,
        targetCurrencyId: String
    ) {
        launch {
            val exchangeResult = currencyExchangeUseCase(
                amount = amount,
                currentCurrencyId = currentCurrencyId,
                targetCurrencyId = targetCurrencyId,
            ).getOrElse {
                CurrencyExchangeResult.Error
            }

            mutableEvent.post(
                Event.CurrencyExchangeTransactionResult(
                    exchangeResult
                )
            )
        }
    }

    companion object {
        private const val RATE_SYNC_PERIOD_MILLIS = 5000L

    }
}
