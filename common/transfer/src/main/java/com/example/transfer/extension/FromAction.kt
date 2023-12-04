package com.example.transfer.extension

import com.example.transfer.model.Transfer
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Transforms an sync into an async transfer operation with coroutines.
 *
 * Transfer.fromAction { syncAction() }
 *
 * @param T Return type of the sync function
 * @param action Representation of the sync function within the lambda
 * @return Transfer<T> Returns an Transfer with data body Type of <T>
 */
suspend fun <T> Transfer.Companion.fromAction(action: () -> T): Transfer<T> =
    coroutineScope {
        async {
            try {
                Transfer.success(action())
            } catch (e: Exception) {
                Transfer.fromException<T>(e)
            }
        }
    }
        .await()

/**
 * Transforms an async into an async transfer operation with coroutines.
 *
 * Transfer.fromAction { syncAction() }
 *
 * @param T Return type of the sync function
 * @param action Representation of the sync function within the lambda
 * @return Transfer<T> Returns an Transfer with data body Type of <T>
 */
suspend fun <T> Transfer.Companion.fromAsyncAction(action: suspend () -> T): Transfer<T> =
    coroutineScope {
        async {
            try {
                Transfer.success(action())
            } catch (e: Exception) {
                Transfer.fromException<T>(e)
            }
        }
    }
        .await()

suspend fun emptyTransfer(): Transfer<Unit> =
    Transfer.fromAction {}

suspend fun <T> transfer(action: () -> T): Transfer<T> =
    Transfer.fromAction(action)

suspend fun <T> asyncTransfer(action: suspend () -> T): Transfer<T> =
    Transfer.fromAsyncAction(action)

fun <T> suspendTransfer(action: () -> T): suspend () -> Transfer<T> =
    suspend { transfer(action) }

fun <T> suspendAsyncTransfer(action: suspend () -> T): suspend () -> Transfer<T> =
    suspend { asyncTransfer(action) }
