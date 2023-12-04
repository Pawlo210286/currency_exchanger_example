package com.example.core.extensions

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

fun <T> MutableStateFlow<T>.post(state: T) {
    value = state
}

suspend fun <T> MutableSharedFlow<T>.postEvent(state: T) {
    emit(state)
}

fun <T> MutableSharedFlow<T>.postEvent(state: T, coroutineContext: CoroutineContext = Dispatchers.IO) {
    CoroutineScope(coroutineContext).launch {
        emit(state)
    }
}

suspend fun <T> MutableSharedFlow<T>.postEvents(states: List<T>) {
    states.forEach { emit(it) }
}

fun <T> MutableSharedFlow<T>.postEvents(states: List<T>, coroutineContext: CoroutineContext = Dispatchers.IO) {
    CoroutineScope(coroutineContext).launch {
        states.forEach { emit(it) }
    }
}

fun <T> MutableStateFlow<T>.switchTo(state: T) {
    if (value != state) post(state)
}
