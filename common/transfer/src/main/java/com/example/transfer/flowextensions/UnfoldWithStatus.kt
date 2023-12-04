package com.example.transfer.flowextensions

import com.example.transfer.extension.fromException
import com.example.transfer.model.FlowTransfer
import com.example.transfer.model.Transfer
import com.example.transfer.model.TransferError
import com.example.transfer.model.TransferStatus
import kotlinx.coroutines.coroutineScope

/**
 * Unfolds an FlowTransfer action from it's data.
 * In this case the status of the transfer is been provided within the lambda.
 *l
 * FlowTransfer<T>.unfold (
 *     success = { status, data ->
 *         doSomethingWith(data)
 *             .inCaseOfSuccess()
 *     },
 *     failure = { status, error ->
 *     when(status){
 *         GENERAL_ERROR -> doSomethingWith(error)
 *         NETWORK_ERROR -> doSomethingElseWith(error)
 *         else -> ...
 *     })
 *
 * @param T Receive type of the function
 * @param success Defines what to do in case of a succeeding transfer action
 * @param failure Defines what to do in case of a failing transfer action
 * @return Returns Unit in every case
 */
suspend fun <T> FlowTransfer<T>.unfoldWithStatus(
    success: suspend (data: T) -> Unit = { _ -> },
    failure: suspend (status: TransferStatus, error: TransferError) -> Unit = { _, _ -> }
): Unit =
    try {
        coroutineScope {
            collect {
                when (it) {
                    is Transfer.Success -> success(it.data)
                    is Transfer.Failure -> failure(it.status, it.error)
                }
            }
        }
    } catch (e: Exception) {
        Transfer.fromException<T>(e).let { exceptionCase ->
            failure(exceptionCase.status, exceptionCase.error)
        }
    }

