package com.example.exchanger.di

import com.example.exchanger.viewmodel.CurrencyExchangeOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::CurrencyExchangeOverviewViewModel)
}