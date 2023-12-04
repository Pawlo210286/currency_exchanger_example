package com.example.transfer.extension

import com.example.transfer.model.Transfer
import kotlinx.coroutines.*
import java.util.concurrent.TimeoutException

private const val MAX_TIMEOUT = 20000L

/**
 * Transforms an operation that waiting for callback into a suspendable coroutine with timeout.
 *
 * Transfer.fromCallbackAction<CallbackReturnType> { continuation ->
 *      something().setListener { resultFromCallback -> continuation.resume(resultFromCallback) }
 *
 * }
 *
 * @param T Return type of the resulting function
 * @param timeout Defines a timeout for the operation to happen in n milliseconds
 * @param action Representation of the function within the lambda
 * @return Transfer<T> Returns an Transfer with data body Type of <T>
 */
suspend fun <T> Transfer.Companion.fromCallbackAction(
    timeout: Long = MAX_TIMEOUT,
    action: (CancellableContinuation<T>) -> Unit,
): Transfer<T> =
    try {
        withTimeoutOrNull(timeout) { suspendCancellableCoroutine { continuation -> action(continuation) } }
            ?.let {
                Transfer.success(it)
            } ?: Transfer.canceled("Failure happened due to Timeout ($timeout ms)", TimeoutException())
    } catch (e: Exception) {
        Transfer.fromException(e)
    }

suspend fun <T> transferFrom(
    timeout: Long = MAX_TIMEOUT,
    action: (CancellableContinuation<T>) -> Unit,
) = Transfer.fromCallbackAction(timeout, action)

