package com.example.transfer.extension

import com.example.transfer.model.Transfer

inline fun <T> Transfer<T>.onEach(onEach: (data: T) -> Unit): Transfer<T> =
    when (this) {
        is Transfer.Success -> try {
            onEach(data)

            this
        } catch (e: Exception) {
            Transfer.fromException(e)
        }
        is Transfer.Failure -> Transfer.Failure(status = status, error = error)
    }

suspend inline fun <T, R> Transfer<T>.onEachContinue(onEach: (T) -> Transfer<R>): Transfer<T> =
    when (this) {
        is Transfer.Success -> try {
            onEach(data)
                .flatTransform { this }
        } catch (e: Exception) {
            Transfer.fromException(e)
        }
        is Transfer.Failure -> Transfer.Failure(status = status, error = error)
    }
