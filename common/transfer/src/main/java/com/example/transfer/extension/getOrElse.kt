package com.example.transfer.extension

import com.example.transfer.model.Transfer
import com.example.transfer.model.TransferError

/**
 * Compose the value of this Transfer or provide a else case if the transfer is failing
 */
inline fun <T> Transfer<T>.getOrElse(failure: (TransferError) -> T): T =
    try {
        when (this) {
            is Transfer.Success -> data
            is Transfer.Failure -> failure(error)
        }
    } catch (e: Exception) {
        failure(Transfer.fromException<T>(e).error)
    }