package com.example.transfer.extension

import com.example.transfer.model.Transfer

/**
 * Transforms a List of Transfers into a Transfer List of object
 *
 * List<Transfer<T>>.toTransferList()
 *
 * @param Transfer<List<T>> Return type of the function
 */
suspend fun <T : Any> List<Transfer<T>>.toTransferList(): Transfer<List<T>> =
    try {
        firstOrNull { it is Transfer.Failure }
            .let { brokenTransfer ->
                brokenTransfer
                    ?.transform { listOf(it) }
                    ?: Transfer.success(mapNotNull { (it as Transfer.Success).data })
            }
    } catch (e: Exception) {
        Transfer.fromException(e)
    }
