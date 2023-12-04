package com.example.transfer.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

typealias FlowTransfer<T> = Flow<Transfer<T>>

suspend fun <T> FlowCollector<Transfer<T>>.emitOnSuccess(transfer: Transfer<T>) {
    if (transfer is Transfer.Success<T>) {
        emit(transfer)
    }
}

