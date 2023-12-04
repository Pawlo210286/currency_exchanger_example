package com.example.transfer.extension

import com.example.transfer.http.extension.authorizationError
import com.example.transfer.http.extension.generalError
import com.example.transfer.http.extension.isClientError
import com.example.transfer.http.extension.isContinue
import com.example.transfer.http.extension.isRedirect
import com.example.transfer.http.extension.isServerError
import com.example.transfer.http.extension.isSuccess
import com.example.transfer.http.extension.networkError
import com.example.transfer.http.extension.notFound
import com.example.transfer.http.extension.resourceConflictError
import com.example.transfer.http.extension.success
import com.example.transfer.http.extension.successNoContent
import com.example.transfer.http.extension.userError
import com.example.transfer.model.Transfer
import retrofit2.Response
import java.net.HttpURLConnection.*

/**
 * Folds a transfer out of a Response
 *
 * @param T Return type of the transfers data body
 * @return Transfer<T> Returns a Transfer with data body Type of <T>
 */
fun <T> Transfer.Companion.fromResponse(action: () -> Response<T>): Transfer<T> {
    return try {
        action().checkSpecificCases()
    } catch (e: Exception) {
        Transfer.fromException(e)
    }
}

/**
 * Folds a transfer out of a Response asynchronous
 *
 * @param T Return type of the transfers data body
 * @return Transfer<T> Returns a Transfer with data body Type of <T>
 */
suspend fun <T> Transfer.Companion.fromAsyncResponse(action: suspend () -> Response<T>): Transfer<T> {
    return try {
        action().checkSpecificCases()
    } catch (e: Exception) {
        Transfer.fromException(e)
    }
}

private fun <T> Response<T>.checkSpecificCases(): Transfer<T> =
    when (code()) {
        HTTP_OK -> success()
        HTTP_CREATED -> success()
        HTTP_NO_CONTENT -> successNoContent()
        HTTP_BAD_REQUEST -> userError()
        HTTP_UNAUTHORIZED -> authorizationError()
        HTTP_FORBIDDEN -> userError()
        HTTP_NOT_FOUND -> notFound()
        HTTP_BAD_METHOD -> userError()
        HTTP_CONFLICT -> resourceConflictError()
        HTTP_CLIENT_TIMEOUT -> networkError()
        HTTP_UNAVAILABLE -> generalError()
        HTTP_GATEWAY_TIMEOUT -> generalError()
        else -> checkGeneralCases()
    }

private fun <T> Response<T>.checkGeneralCases(): Transfer<T> =
    with(code()) {
        when {
            isContinue() -> success()
            isSuccess() -> success()
            isRedirect() -> success()
            isClientError() -> userError()
            isServerError() -> generalError()
            else -> generalError()
        }
    }


