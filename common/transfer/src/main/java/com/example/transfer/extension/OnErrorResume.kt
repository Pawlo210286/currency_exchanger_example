package com.example.transfer.extension

import com.example.transfer.model.Transfer

suspend fun <T> Transfer<T>.onErrorResume(fallbackSupplier: (Transfer.Failure<T>) -> Transfer<T>): Transfer<T> =
    when (this) {
        is Transfer.Success -> this
        is Transfer.Failure -> fallbackSupplier(this)
    }

fun <T> Transfer<T>.onErrorResumeSync(fallbackSupplier: (Transfer.Failure<T>) -> Transfer<T>): Transfer<T> =
    when (this) {
        is Transfer.Success -> this
        is Transfer.Failure -> fallbackSupplier(this)
    }

suspend fun <T> Transfer<T>.onErrorResumeAsync(fallbackSupplier: suspend (Transfer.Failure<T>) -> Transfer<T>): Transfer<T> =
    when (this) {
        is Transfer.Success -> this
        is Transfer.Failure -> fallbackSupplier(this)
    }

suspend fun <T> Transfer<T>.onErrorReturn(fallbackSupplier: (Transfer.Failure<T>) -> T): Transfer<T> =
    when (this) {
        is Transfer.Success -> this
        is Transfer.Failure -> transfer { fallbackSupplier(this) }
    }

suspend fun <T> Transfer<T>.onErrorSkipResult(): Transfer<Unit> =
    when (this) {
        is Transfer.Success -> skipResult()
        is Transfer.Failure -> transfer { Unit }
    }

suspend fun <T> Transfer<T>.onErrorReturnAsync(fallbackSupplier: suspend (Transfer.Failure<T>) -> T): Transfer<T> =
    when (this) {
        is Transfer.Success -> this
        is Transfer.Failure -> Transfer.fromAsyncAction { fallbackSupplier(this) }
    }

suspend fun <T> Transfer<T>.onErrorThrow(fallbackSupplier: (Transfer.Failure<T>) -> Throwable): Transfer<T> =
    when (this) {
        is Transfer.Success -> this
        is Transfer.Failure -> Transfer.fromException(fallbackSupplier(this))
    }
