package com.example.transfer.flowextensions

import com.example.transfer.extension.fromException
import com.example.transfer.model.FlowTransfer
import com.example.transfer.model.Transfer
import com.example.transfer.model.TransferError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope

/**
 * Unfolds a FlowTransfer.
 *
 * () -> FlowTransfer<T>.unfold (
 *     success = { data ->
 *         doSomethingWith(data)
 *             .inCaseOfSuccess()
 *     },
 *     failure = { error ->
 *         doSomethingWith(error)
 *             .inCaseOfError()
 *     })
 *
 * @param T Receive type of the function
 * @param success Defines what to do in case of a succeeding transfer
 * @param failure Defines what to do in case of a failing transfer
 * @return Returns Unit in every case
 */
suspend fun <T> FlowTransfer<T>.unfold(
    success: suspend (data: T) -> Unit = { },
    failure: suspend (error: TransferError) -> Unit = { }
): Unit =
    try {
        coroutineScope {
            collect {
                when (it) {
                    is Transfer.Success -> success(it.data)
                    is Transfer.Failure -> failure(it.error)
                }
            }
        }
    } catch (e: CancellationException) {
        /**
         * Should not be handled due to the original
         * structure of coroutines and flow.
         */
    } catch (e: Exception) {
        failure(Transfer.fromException<T>(e).error)
    }
