package com.example.transfer.extension

import com.example.transfer.model.Transfer
import com.example.transfer.model.TransferError
import com.example.transfer.model.TransferStatus

inline fun <T, R> Transfer<T>.fold(
    onError: (TransferStatus, TransferError) -> R,
    onSuccess: (T) -> R
): R = when (this) {
    is Transfer.Failure -> onError(status, error)
    is Transfer.Success -> onSuccess(data)
}
