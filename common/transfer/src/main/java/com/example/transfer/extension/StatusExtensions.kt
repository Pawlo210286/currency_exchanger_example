package com.example.transfer.extension

import com.example.transfer.model.Transfer
import com.example.transfer.model.TransferError
import com.example.transfer.model.TransferGatewayError
import com.example.transfer.model.TransferStatus
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException

/**
 * Creates a new Transfer from status success
 *
 * @param T Type of the transfers data body
 * @param data Data body of the transfer
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.success(data: T): Transfer.Success<T> =
    Transfer.Success(status = TransferStatus.SUCCESS, data = data)

/**
 * Create a new Transfer to handle the new gateway error
 *
 * @param errors list of TransferGatewayError which comes from the new gateway based on GraphQL
 *
 * @return Transfer<T> Always returns the first error of the list as a transfer.
 */
fun <T> Transfer.Companion.gatewayError(
    errors: List<TransferGatewayError>,
): Transfer.Failure<T> {
    if (errors.isEmpty()) {
        com.example.transfer.common.TransferConfig.logger?.error(
            message = "No errors found. Should never happen.",
            cause = IllegalStateException("No error found"),
            tag = "@Transfer.Companion.gatewayError"
        )
        return Transfer.Failure(
            status = TransferStatus.IO_ERROR,
            error = TransferError(
                message = "No gateway errors found.",
                cause = IllegalStateException("No gateway errors found.")
            )
        )
    }

    if (errors.count() > 1) {
        com.example.transfer.common.TransferConfig.logger?.error(
            message = "More than one error message",
            cause = IllegalStateException("receive more than one error: $errors"),
            tag = "@Transfer.Companion.gatewayError"
        )
    }

    val error = errors.first()

    val transferStatus: TransferStatus = when (error.code) {
        HttpURLConnection.HTTP_BAD_REQUEST,
        HttpURLConnection.HTTP_BAD_METHOD,
        HttpURLConnection.HTTP_FORBIDDEN,
        -> TransferStatus.USER_ERROR
        HttpURLConnection.HTTP_NOT_FOUND -> TransferStatus.NOT_FOUND
        HttpURLConnection.HTTP_UNAUTHORIZED,
        -> TransferStatus.AUTHORIZATION_ERROR
        HttpURLConnection.HTTP_CONFLICT -> TransferStatus.RESOURCE_CONFLICT
        else -> TransferStatus.GENERAL_ERROR
    }

    return Transfer.Failure(
        status = transferStatus,
        error = TransferError(
            message = error.message,
            cause = IllegalStateException(error.message)
        )
    )
}

/**
 * Creates a new Transfer from status general error
 *
 * @param T Type of the transfers data body
 * @param message Message of the error
 * @param cause Stacktrace of the error
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.generalError(
    message: String,
    cause: Throwable = IllegalStateException(message),
): Transfer.Failure<T> =
    Transfer.Failure(status = TransferStatus.GENERAL_ERROR, error = TransferError(message, cause))

/**
 * Creates a new Transfer from status network error
 *
 * @param T Type of the transfers data body
 * @param message Message of the error
 * @param cause Stacktrace of the error
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.networkError(
    message: String,
    cause: Throwable = ConnectException(message),
): Transfer.Failure<T> =
    Transfer.Failure(status = TransferStatus.NETWORK_ERROR, error = TransferError(message, cause))

/**
 * Creates a new Transfer from status timeout error
 *
 * @param T Type of the transfers data body
 * @param message Message of the error
 * @param cause Stacktrace of the error
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.timeoutError(
    message: String,
    cause: Throwable = TimeoutException(message),
): Transfer.Failure<T> =
    Transfer.Failure(status = TransferStatus.TIMEOUT_ERROR, error = TransferError(message, cause))

/**
 * Creates a new Transfer from status io error
 *
 * @param T Type of the transfers data body
 * @param error TransferError That's the error
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.ioError(error: TransferError): Transfer.Failure<T> =
    Transfer.Failure(status = TransferStatus.IO_ERROR, error = error)

/**
 * Creates a new Transfer from status io error
 *
 * @param T Type of the transfers data body
 * @param message Message of the error
 * @param cause Stacktrace of the error
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.ioError(
    message: String,
    cause: Throwable = IOException(message),
): Transfer.Failure<T> =
    Transfer.Failure(status = TransferStatus.IO_ERROR, error = TransferError(message, cause))

/**
 * Creates a new Transfer from status canceled
 *
 * @param T Type of the transfers data body
 * @param error TransferError That's the error
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.canceled(error: TransferError): Transfer.Failure<T> =
    Transfer.Failure(status = TransferStatus.CANCELED, error = error)

/**
 * Creates a new Transfer from status canceled
 *
 * @param T Type of the transfers data body
 * @param message Message of the error
 * @param cause Stacktrace of the error
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.canceled(
    message: String,
    cause: Throwable = CancellationException(message),
): Transfer.Failure<T> =
    Transfer.Failure(status = TransferStatus.CANCELED, error = TransferError(message, cause))

/**
 * Creates a new Transfer from status user error
 *
 * @param T Type of the transfers data body
 * @param message Message of the error
 * @param cause Stacktrace of the error
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.userError(
    message: String,
    cause: Throwable = IllegalStateException(message),
): Transfer.Failure<T> =
    Transfer.Failure(status = TransferStatus.USER_ERROR, error = TransferError(message, cause))

/**
 * Creates a new Transfer from status authorization error
 * We could create an AuthenticationRequiredException() but because we are in a kotlin module we can only create a SecurityException
 *
 * @param T Type of the transfers data body
 * @param message Message of the error
 * @param cause Stacktrace of the error
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.authorizationError(
    message: String,
    cause: Throwable = SecurityException(message),
): Transfer.Failure<T> =
    Transfer.Failure(status = TransferStatus.AUTHORIZATION_ERROR, error = TransferError(message, cause))

/**
 * Creates a new Transfer from status not-found
 *
 * @param T Type of the transfers data body
 * @param message Message of the error
 * @param cause Stacktrace of the error
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.notFound(
    message: String,
    cause: Throwable = IOException(message),
): Transfer.Failure<T> =
    Transfer.Failure(status = TransferStatus.NOT_FOUND, error = TransferError(message, cause))

/**
 * Creates a new Transfer from status customer already exists
 *
 * @param T Type of the transfers data body
 * @param message Message of the error
 * @param cause Stacktrace of the error
 * @return Transfer<T> Returns a transfer with type <T>
 */
fun <T> Transfer.Companion.resourceConflictError(
    message: String,
    cause: Throwable = IOException(message),
): Transfer.Failure<T> =
    Transfer.Failure(status = TransferStatus.RESOURCE_CONFLICT, error = TransferError(message, cause))