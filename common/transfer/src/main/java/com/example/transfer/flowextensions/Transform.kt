package com.example.transfer.flowextensions

import com.example.transfer.extension.transform
import com.example.transfer.model.FlowTransfer
import kotlinx.coroutines.flow.transform

/**
 * map [FlowTransfer]<T> to [FlowTransfer]<R>
 *
 * @param transformer function to apply to get from value T to R
 *
 * @see kotlinx.coroutines.flow.transform
 * @see com.example.app.transfer.extension.transform
 */
inline fun <T, R> FlowTransfer<T>.transform(crossinline transformer: (T) -> R): FlowTransfer<R> =
    transform { emit(it.transform(transformer)) }