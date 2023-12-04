package com.example.transfer.model

enum class TransferStatus {
    AUTHORIZATION_ERROR,
    CANCELED,
    GENERAL_ERROR,
    IO_ERROR,
    NETWORK_ERROR,
    NOT_FOUND,
    RESOURCE_CONFLICT,
    SUCCESS,
    TIMEOUT_ERROR,
    USER_ERROR,
}