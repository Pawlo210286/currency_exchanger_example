package com.example.transfer.extension

import com.example.transfer.model.Transfer
import java.io.IOException
import java.io.InterruptedIOException
import java.net.*
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException

fun <T> Transfer.Companion.fromException(
    throwable: Throwable,
): Transfer.Failure<T> =
    when (throwable) {
        is CancellationException -> Transfer.canceled(
            message = throwable.message ?: "Transfer has been canceled",
            cause = throwable,
        )
        is UnknownHostException, is ConnectException -> Transfer.networkError(
            message = throwable.message ?: "Handling Unknown Host Error",
            cause = throwable,
        )
        is SocketTimeoutException -> Transfer.timeoutError(
            message = throwable.message ?: "Handling Socket Timeout",
            cause = throwable,
        )
        is InterruptedIOException -> Transfer.timeoutError(
            message = throwable.message ?: "Handling Interrupted IO Error",
            cause = throwable,
        )
        is TimeoutException -> Transfer.timeoutError(
            message = throwable.message ?: "Handling Unknown Host Error",
            cause = throwable,
        )
        is IOException -> Transfer.ioError(
            message = throwable.message ?: "Handling IO Error",
            cause = throwable,
        )
        else -> Transfer.generalError<T>(
            message = throwable.message ?: "Failure happened while transforming data",
            cause = throwable,
        )
    }
        .also {
            if (shouldLogError(throwable))
                com.example.transfer.common.TransferConfig.logger?.error(
                    message = it.getFormattedMessage(),
                    cause = throwable,
                    tag = "Transfer.fromException"
                )
            else
                com.example.transfer.common.TransferConfig.logger?.info(
                    message = it.getFormattedMessage(),
                    cause = throwable,
                    tag = "Transfer.fromException"
                )
        }

private fun<T> Transfer.Failure<T>.getFormattedMessage(): String = "#### Unexpected exception in Transfer ${error.message}"

private fun shouldLogError(throwable: Throwable): Boolean =
    when (throwable) {
        is CancellationException,
        is UnknownHostException,
        is SocketTimeoutException,
        is InterruptedIOException,
        is TimeoutException,
        is SSLException,
        is ConnectException,
        is IOException,
        -> false
        else -> true
    }
