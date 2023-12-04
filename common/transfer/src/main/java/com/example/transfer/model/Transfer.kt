package com.example.transfer.model

sealed class Transfer<T> {

    abstract val status: TransferStatus

    data class Success<T>(
        override val status: TransferStatus,
        val data: T,
    ) : Transfer<T>()

    data class Failure<T>(
        override val status: TransferStatus,
        val error: TransferError,
    ) : Transfer<T>()

    companion object
}
