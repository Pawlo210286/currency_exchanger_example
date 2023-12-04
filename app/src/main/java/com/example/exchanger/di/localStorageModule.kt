package com.example.exchanger.di

import com.example.exchanger.datasource.local.ProfileStateProvider
import com.example.exchanger.datasource.local.ProfileStateProviderImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val localStorageModule = module {

    single<ProfileStateProvider> { ProfileStateProviderImpl(get(named(IO_CONTEXT))) }
}
