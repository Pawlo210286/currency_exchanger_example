package com.example.transfer.flowextensions

import com.example.transfer.extension.flatTransform
import com.example.transfer.model.FlowTransfer
import com.example.transfer.model.Transfer
import kotlinx.coroutines.flow.transform

/**
 * Transforms the data body of the transfer from T to FlowTransfer<R>
 *
 * FlowTransfer<T>.flatTransform { data ->
 *     firstData.doSomething()
 * }
 *
 * @param T Receive type of the function
 * @param R Return type of the function
 * @param transformer Defines the transformation from T to Transfer<R> within a lambda
 * @return FlowTransfer<R> Returns a Flow of Transfer<R>
 *
 * @see Transfer.flatTransform
 */
suspend inline fun <T, R> FlowTransfer<T>.flatTransform(crossinline transformer: suspend (data: T) -> Transfer<R>): FlowTransfer<R> =
    transform { transfer -> emit(transfer.flatTransform { data -> transformer(data) }) }