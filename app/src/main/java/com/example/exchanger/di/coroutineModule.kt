package com.example.exchanger.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val IO_DISPATCHER = "IO_DISPATCHER"
val IO_CONTEXT = "IO_CONTEXT"

val coroutineModule = module {
    single(named(IO_DISPATCHER)) { Dispatchers.IO }
    single(named(IO_CONTEXT)) {
        val coroutineDispatcher = get<CoroutineDispatcher>(named(IO_DISPATCHER))
        coroutineDispatcher + SupervisorJob()
    }
}