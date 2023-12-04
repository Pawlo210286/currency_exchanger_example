@file:Suppress("RedundantAsync")

package com.example.transfer.extension

import com.example.transfer.coroutines.runAsync
import com.example.transfer.model.Transfer
import com.example.transfer.model.TransferError
import com.example.transfer.tuples.tupled
import com.example.transfer.types.SuspendTransferF
import com.example.multifunctions.letCheckNull
import com.example.multifunctions.models.Quad
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Unfolds an Transfer action from it's data.
 *
 * () -> Transfer<T>.unfold (
 *     success = { data ->
 *         doSomethingWith(data)
 *             .inCaseOfSuccess()
 *     },
 *     failure = { error ->
 *         doSomethingWith(error)
 *             .inCaseOfError()
 *     })
 *
 * @param T Receive type of the function
 * @param success Defines what to do in case of a succeeding transfer action
 * @param failure Defines what to do in case of a failing transfer action
 * @return Returns Unit in every case
 */
suspend fun <T> (SuspendTransferF<T>).unfold(
    success: suspend (data: T) -> Unit = { },
    failure: suspend (error: TransferError) -> Unit = { },
): Unit =
    try {
        coroutineScope {
            with(async { this@unfold() }.await()) {
                when (this) {
                    is Transfer.Success -> success(data)
                    is Transfer.Failure -> failure(error)
                }
            }
        }
    } catch (e: Exception) {
        failure(Transfer.fromException<T>(e).error)
    }

fun <T> Transfer<T>.unfold(
    success: (data: T) -> Unit = {},
    failure: (error: TransferError) -> Unit = { _ -> },
) {
    when (this) {
        is Transfer.Success -> success(data)
        is Transfer.Failure -> failure(error)
    }
}

/**
 * Unfolds an Transfer action Pair<() -> Transfer<A>, () -> Transfer<B>> from it's data separately.
 * When one transaction is failing the whole unfold operation will fail.
 *
 * Pair<() -> Transfer<A>, () -> Transfer<B>>.unfold (
 *     success = { firstData, secondData ->
 *         firstData.doSomethingWith(secondData)
 *             .inCaseOfSuccess()
 *     },
 *     failure = { error ->
 *         doSomethingWith(error)
 *             .inCaseOfError()
 *     })
 *
 * @param A First receive type of the function
 * @param B Second receive type of the function
 * @param success Defines what to do in case of a succeeding transfer action
 * @param failure Defines what to do in case of a failing transfer action
 * @return Returns Unit in every case
 */
suspend fun <A, B> Pair<SuspendTransferF<A>, SuspendTransferF<B>>.unfold(
    success: (firstData: A, secondData: B) -> Unit = { _, _ -> },
    failure: (error: TransferError) -> Unit = { },
) =
    try {
        coroutineScope {
            tupled(
                first,
                second,
            )
                .runAsync()
                .letCheckNull { resultA, resultB ->
                    when {
                        resultA is Transfer.Failure -> failure(resultA.error)
                        resultB is Transfer.Failure -> failure(resultB.error)
                        resultA is Transfer.Success && resultB is Transfer.Success -> {
                            success(resultA.data, resultB.data)
                        }
                    }
                } ?: failure(TransferError("Failure happened while unfolding transfer"))
        }
    } catch (e: Exception) {
        failure(Transfer.fromException<Pair<SuspendTransferF<A>, SuspendTransferF<B>>>(e).error)
    }

/**
 * Unfolds an Transfer action Triple<() -> Transfer<A>, () -> Transfer<B>, () -> Transfer<C>> from it's data separately.
 * When one transaction is failing the whole unfold operation will fail.
 *
 * Triple<() -> Transfer<A>, () -> Transfer<B>, () -> Transfer<C>>.unfold (
 *     success = { firstData, secondData, thirdData ->
 *         firstData.doSomethingWith(secondData)
 *             .whichInvolves(thirdData)
 *             .inCaseOfSuccess()
 *     },
 *     failure = { error ->
 *         doSomethingWith(error)
 *             .inCaseOfError()
 *     })
 *
 * @param A First receive type of the function
 * @param B Second receive type of the function
 * @param C Third receive type of the function
 * @param success Defines what to do in case of a succeeding transfer action
 * @param failure Defines what to do in case of a failing transfer action
 * @return Returns Unit in every case
 */
suspend fun <A, B, C> Triple<SuspendTransferF<A>, SuspendTransferF<B>, SuspendTransferF<C>>.unfold(
    success: (firstData: A, secondData: B, thirdData: C) -> Unit = { _, _, _ -> },
    failure: (error: TransferError) -> Unit = { },
) =
    try {
        coroutineScope {
            tupled(
                first,
                second,
                third,
            )
                .runAsync()
                .letCheckNull { resultA, resultB, resultC ->
                    when {
                        resultA is Transfer.Failure -> failure(resultA.error)
                        resultB is Transfer.Failure -> failure(resultB.error)
                        resultC is Transfer.Failure -> failure(resultC.error)
                        resultA is Transfer.Success && resultB is Transfer.Success && resultC is Transfer.Success -> {
                            success(resultA.data, resultB.data, resultC.data)
                        }
                    }
                } ?: failure(TransferError("Failure happened while unfolding transfer"))
        }
    } catch (e: Exception) {
        failure(Transfer.fromException<Triple<SuspendTransferF<A>, SuspendTransferF<B>, SuspendTransferF<C>>>(e).error)
    }

/**
 * Unfolds an Transfer action Quad<() -> Transfer<A>, () -> Transfer<B>, () -> Transfer<C>> from it's data separately.
 * When one transaction is failing the whole unfold operation will fail.
 *
 * Quad<() -> Transfer<A>, () -> Transfer<B>, () -> Transfer<C>, () -> Transfer<D>>.unfold (
 *     success = { firstData, secondData, thirdData, fourthData ->
 *         firstData.doSomethingWith(secondData)
 *             .whichInvolves(thirdData)
 *             .inCaseOfSuccess()
 *     },
 *     failure = { error ->
 *         doSomethingWith(error)
 *             .inCaseOfError()
 *     })
 *
 * @param A First receive type of the function
 * @param B Second receive type of the function
 * @param C Third receive type of the function
 * @param success Defines what to do in case of a succeeding transfer action
 * @param failure Defines what to do in case of a failing transfer action
 * @return Returns Unit in every case
 */
suspend fun <A, B, C, D> Quad<SuspendTransferF<A>, SuspendTransferF<B>, SuspendTransferF<C>, SuspendTransferF<D>>.unfold(
    success: (firstData: A, secondData: B, thirdData: C, fourth: D) -> Unit = { _, _, _, _ -> },
    failure: (error: TransferError) -> Unit = { },
) =
    try {
        coroutineScope {
            tupled(
                first,
                second,
                third,
                fourth,
            )
                .runAsync()
                .letCheckNull { resultA, resultB, resultC, resultD ->
                    when {
                        resultA is Transfer.Failure -> failure(resultA.error)
                        resultB is Transfer.Failure -> failure(resultB.error)
                        resultC is Transfer.Failure -> failure(resultC.error)
                        resultD is Transfer.Failure -> failure(resultD.error)
                        resultA is Transfer.Success && resultB is Transfer.Success && resultC is Transfer.Success && resultD is Transfer.Success -> {
                            success(resultA.data, resultB.data, resultC.data, resultD.data)
                        }
                    }
                } ?: failure(TransferError("Failure happened while unfolding transfer"))
        }
    } catch (e: Exception) {
        failure(Transfer.fromException<Quad<SuspendTransferF<A>, SuspendTransferF<B>, SuspendTransferF<C>, SuspendTransferF<D>>>(e).error)
    }