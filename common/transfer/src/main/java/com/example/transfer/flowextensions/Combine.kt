package com.example.transfer.flowextensions

import com.example.transfer.extension.combine
import com.example.transfer.model.FlowTransfer
import kotlinx.coroutines.flow.combine

/**
 * Combines two [FlowTransfer]s into one [FlowTransfer]
 *
 * Pair(FlowTransfer<A>, FlowTransfer<B>).combine()
 *
 * @return [FlowTransfer]
 */
fun <A, B, R> Pair<FlowTransfer<A>, FlowTransfer<B>>.combine(transform: (a: A, b: B) -> R): FlowTransfer<R> =
    combine(first, second) { transfer1, transfer2 -> Pair(transfer1, transfer2).combine(transform) }
