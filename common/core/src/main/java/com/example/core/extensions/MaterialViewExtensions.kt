package com.example.core.extensions

import android.util.Log
import android.view.View
import com.example.transfer.extension.ExecuteFirstDataHolder
import com.example.transfer.extension.ExecuteFirstDataHolder.Companion.executeFirst

private const val DELAY_RIPPLE = 140L
private val navigationExecuteFirstDataHolder = ExecuteFirstDataHolder(DELAY_RIPPLE * 2)

/**
 * [View.setDelayedOnClickListener] adds a delay to the click listener action.
 * The intention is to add a delay in order to allow user to see the ripple effect before
 * the real action takes place.
 */

fun View?.setDelayedOnClickListener(action: (() -> Unit)?) {
    this?.apply {
        action?.let { nonNullAction ->
            setOnClickListener {
                executeFirst(navigationExecuteFirstDataHolder) {
                    postDelayed(tryAction(nonNullAction), DELAY_RIPPLE)
                }
            }
        } ?: setOnClickListener(null)
    }
}

private fun tryAction(action: (() -> Unit)?): (() -> Unit) = {
    try {
        action?.invoke()
    } catch (e: IllegalStateException) {
        Log.d("UNEXPECTED_ERROR_TAG", e.message ?: "Error caused on post delayed click action", e.cause)
    }
}
