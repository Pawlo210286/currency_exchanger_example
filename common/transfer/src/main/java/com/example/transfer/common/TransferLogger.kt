package com.example.transfer.common

interface TransferLogger {

    fun verbose(message: String, cause: Throwable, tag: String? = null)

    fun debug(message: String, cause: Throwable, tag: String? = null)

    fun info(message: String, cause: Throwable, tag: String? = null)

    fun warn(message: String, cause: Throwable, tag: String? = null)

    fun error(message: String, cause: Throwable, tag: String? = null)
}
