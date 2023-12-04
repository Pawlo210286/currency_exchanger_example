package com.example.transfer.model

data class TransferGatewayError(
    val message: String,
    val code: Int?,
    val classification: String,
)

data class TransferError(
    val message: String,
    val cause: Throwable = IllegalStateException(DEFAULT_MESSAGE),
) {

    companion object {

        private const val DEFAULT_MESSAGE = "Unknown exception appeared in Transfer."

        fun fromThrowable(cause: Throwable?) =
            TransferError(
                message = cause?.message ?: DEFAULT_MESSAGE,
                cause = cause ?: IllegalStateException(DEFAULT_MESSAGE),
            )
    }
}
