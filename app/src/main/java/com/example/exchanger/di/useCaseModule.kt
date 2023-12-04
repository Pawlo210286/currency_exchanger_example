package com.example.exchanger.di

import com.example.exchanger.usecase.CurrencyExchangeUseCase
import com.example.exchanger.usecase.GetCurrencyRateUseCase
import com.example.exchanger.usecase.GetProfileStateUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetCurrencyRateUseCase(get(named(IO_DISPATCHER)), get()) }
    factory { GetProfileStateUseCase(get(named(IO_DISPATCHER)), get()) }
    factory { CurrencyExchangeUseCase(get(named(IO_DISPATCHER)), get(), get()) }
}
