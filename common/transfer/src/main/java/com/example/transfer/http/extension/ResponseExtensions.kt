package com.example.transfer.http.extension

import com.example.transfer.extension.authorizationError
import com.example.transfer.extension.generalError
import com.example.transfer.extension.networkError
import com.example.transfer.extension.notFound
import com.example.transfer.extension.resourceConflictError
import com.example.transfer.extension.success
import com.example.transfer.extension.userError
import com.example.transfer.model.Transfer
import retrofit2.Response

fun <T> Response<T>.success(): Transfer<T> =
    try {
        body()?.let { nunNullBody ->
            Transfer.success(nunNullBody)
        } ?: Transfer.generalError("Invalid data")
    } catch (cause: Exception) {
        Transfer.generalError(
            message = "Cannot pack success transfer",
            cause = cause,
        )
    }

/**
 * HTTP_NO_CONTENT responses are only valid for requests returning Unit because
 * there is no body to parse.
 *
 * Unfortunately there is no way to catch a wrong use of the API.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Response<T>.successNoContent(): Transfer<T> = Transfer.success(Unit as T)

fun <T> Response<T>.notFound(): Transfer<T> =
    try {
        Transfer.notFound(message = message())
    } catch (cause: Exception) {
        Transfer.generalError(
            message = "Cannot pack not-found transfer",
            cause = cause,
        )
    }

fun <T> Response<T>.userError(): Transfer<T> =
    try {
        Transfer.userError(
            message = if (message().trim().isEmpty()) {
                errorBody()?.string() ?: ""
            } else message(),
        )
    } catch (cause: Exception) {
        Transfer.generalError(
            message = "Cannot pack user error transfer",
            cause = cause,
        )
    }

fun <T> Response<T>.authorizationError(): Transfer<T> =
    try {
        Transfer.authorizationError(message = message())
    } catch (cause: Exception) {
        Transfer.generalError(
            message = "Cannot pack authentication error transfer",
            cause = cause,
        )
    }

fun <T> Response<T>.networkError(): Transfer<T> =
    try {
        Transfer.networkError(message = message())
    } catch (cause: Exception) {
        Transfer.generalError(
            message = "Cannot pack user error transfer",
            cause = cause,
        )
    }

fun <T> Response<T>.generalError(): Transfer<T> =
    try {
        Transfer.generalError(message = message())
    } catch (cause: Exception) {
        Transfer.generalError(
            message = "Cannot pack general error transfer",
            cause = cause,
        )
    }

fun <T> Response<T>.resourceConflictError(): Transfer<T> =
    try {
        Transfer.resourceConflictError(message = message())
    } catch (cause: Exception) {
        Transfer.generalError(
            message = "Cannot pack resource conflict error transfer",
            cause = cause,
        )
    }
