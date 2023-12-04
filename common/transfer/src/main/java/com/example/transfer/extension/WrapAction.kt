package com.example.transfer.extension

import com.example.transfer.model.Transfer

/**
 * Performs and wrap a sync operation
 *
 * Transfer.wrapAction { action() }
 *
 * @param T Return type of the function
 * @param action Representation of the sync function within the lambda
 * @return Transfer<T> Returns an Transfer with data body Type of <T>
 */

fun <T> Transfer.Companion.wrapAction(action: () -> T): Transfer<T> =
    try {
        Transfer.success(action())
    } catch (e: Exception) {
        Transfer.fromException(e)
    }