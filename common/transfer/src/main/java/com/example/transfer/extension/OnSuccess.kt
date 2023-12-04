package com.example.transfer.extension

import com.example.transfer.model.Transfer

suspend fun <T> Transfer<T>.onSuccess(onSuccessSupplier: (Transfer.Success<T>) -> Transfer<T>): Transfer<T> =
    when (this) {
        is Transfer.Success -> onSuccessSupplier(this)
        is Transfer.Failure -> this
    }

suspend fun <T, R> Transfer<T>.onSuccessResume(onSuccessSupplier: suspend (data: T) -> Transfer<R>): Transfer<R> =
    when (this) {
        is Transfer.Success -> onSuccessSupplier(data)
        is Transfer.Failure -> Transfer.Failure(status = this.status, error = this.error)
    }
