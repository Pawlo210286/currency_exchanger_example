@file:Suppress("RedundantAsync")

package com.example.transfer.extension

import com.example.multifunctions.letCheckNull
import com.example.multifunctions.models.Quad
import com.example.transfer.model.Transfer
import com.example.transfer.model.TransferError
import com.example.transfer.model.TransferStatus
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Unfolds an Transfer action from it's data.
 * In this case the status of the transfer is been provided within the lambda.
 *
 * Transfer<T>.unfold (
 *     success = { status, data ->
 *         doSomethingWith(data)
 *             .inCaseOfSuccess()
 *     },
 *     failure = { status, error ->
 *     when(status){
 *         GENERAL_ERROR -> doSomethingWith(error)
 *         NETWORK_ERROR -> doSomethingElseWith(error)
 *         else -> ...
 *     })
 *
 * @param T Receive type of the function
 * @param success Defines what to do in case of a succeeding transfer action
 * @param failure Defines what to do in case of a failing transfer action
 * @return Returns Unit in every case
 */
suspend fun <T> (suspend () -> Transfer<T>).unfoldWithStatus(
    success: suspend (data: T) -> Unit = { _ -> },
    failure: suspend (status: TransferStatus, error: TransferError) -> Unit = { _, _ -> },
): Unit =
    try {
        coroutineScope {
            with(async { this@unfoldWithStatus() }.await()) {
                when (this) {
                    is Transfer.Success -> success(data)
                    is Transfer.Failure -> failure(status, error)
                }
            }
        }
    } catch (e: Exception) {
        Transfer.fromException<T>(e).let { exceptionCase ->
            failure(exceptionCase.status, exceptionCase.error)
        }
    }

fun <T> Transfer<T>.unfoldWithStatus(
    success: (data: T) -> Unit = {},
    failure: (status: TransferStatus, error: TransferError) -> Unit = { _, _ -> },
) {
    when (this) {
        is Transfer.Success -> success(data)
        is Transfer.Failure -> failure(status, error)
    }
}

/**
 * Unfolds an Transfer action Pair<() -> Transfer<A>, () -> Transfer<B>> from it's data separately.
 * When one transaction is failing the whole unfold operation will fail.
 * In this case the status of the transfer is been provided within the lambda.
 *
 * Pair<() -> Transfer<A>, () -> Transfer<B>>.unfold (
 *     success = { status, firstData, secondData ->
 *         firstData.doSomethingWith(secondData)
 *             .inCaseOfSuccess()
 *     },
 *     failure = { status, error ->
 *     when(status){
 *         GENERAL_ERROR -> doSomethingWith(error)
 *         NETWORK_ERROR -> doSomethingElseWith(error)
 *         else -> ...
 *     })
 *
 * @param A First receive type of the function
 * @param B Second receive type of the function
 * @param success Defines what to do in case of a succeeding transfer action
 * @param failure Defines what to do in case of a failing transfer action
 * @return Returns Unit in every case
 */
suspend fun <A, B> Pair<suspend () -> Transfer<A>, suspend () -> Transfer<B>>.unfoldWithStatus(
    success: suspend (firstData: A, secondData: B) -> Unit = { _, _ -> },
    failure: suspend (status: TransferStatus, error: TransferError) -> Unit = { _, _ -> },
): Unit =
    try {
        coroutineScope {
            Pair(
                async { first() }.await(),
                async { second() }.await()
            )
                .letCheckNull { resultA, resultB ->
                    when {
                        resultA is Transfer.Failure -> failure(resultA.status, resultA.error)
                        resultB is Transfer.Failure -> failure(resultB.status, resultB.error)
                        resultA is Transfer.Success && resultB is Transfer.Success ->
                            success(resultA.data, resultB.data)
                    }
                } ?: failure(TransferStatus.GENERAL_ERROR, TransferError("Failure happened while unfolding transfer"))
        }
    } catch (e: Exception) {
        Transfer.fromException<Pair<suspend () -> Transfer<A>, suspend () -> Transfer<B>>>(e).let { exceptionCase ->
            failure(exceptionCase.status, exceptionCase.error)
        }
    }

/**
 * Unfolds an Transfer action Triple<() -> Transfer<A>, () -> Transfer<B>, () -> Transfer<C>> from it's data separately.
 * When one transaction is failing the whole unfold operation will fail.
 * In this case the status of the transfer is been provided within the lambda.
 *
 * Triple<() -> Transfer<A>, () -> Transfer<B>, () -> Transfer<C>>.unfold (
 *     success = { firstData, secondData, thirdData ->
 *         firstData.doSomethingWith(secondData)
 *             .whichInvolves(thirdData)
 *             .inCaseOfSuccess()
 *     },
 *     failure = { status, error ->
 *     when(status){
 *         GENERAL_ERROR -> doSomethingWith(error)
 *         NETWORK_ERROR -> doSomethingElseWith(error)
 *         else -> ...
 *     })
 *
 * @param A First receive type of the function
 * @param B Second receive type of the function
 * @param C Third receive type of the function
 * @param success Defines what to do in case of a succeeding transfer action
 * @param failure Defines what to do in case of a failing transfer action
 * @return Returns Unit in every case
 */
suspend fun <A, B, C> Triple<suspend () -> Transfer<A>, suspend () -> Transfer<B>, suspend () -> Transfer<C>>.unfoldWithStatus(
    success: suspend (firstData: A, secondData: B, thirdData: C) -> Unit = { _, _, _ -> },
    failure: suspend (status: TransferStatus, error: TransferError) -> Unit = { _, _ -> },
): Unit =
    try {
        coroutineScope {
            Triple(
                async { first() }.await(),
                async { second() }.await(),
                async { third() }.await()
            )
                .letCheckNull { resultA: Transfer<A>, resultB: Transfer<B>, resultC: Transfer<C> ->
                    when {
                        resultA is Transfer.Failure -> failure(resultA.status, resultA.error)
                        resultB is Transfer.Failure -> failure(resultB.status, resultB.error)
                        resultC is Transfer.Failure -> failure(resultC.status, resultC.error)
                        resultA is Transfer.Success && resultB is Transfer.Success && resultC is Transfer.Success ->
                            success(resultA.data, resultB.data, resultC.data)
                    }
                } ?: failure(TransferStatus.GENERAL_ERROR, TransferError("Failure happened while unfolding transfer"))
        }
    } catch (e: Exception) {
        Transfer.fromException<Triple<suspend () -> Transfer<A>, suspend () -> Transfer<B>, suspend () -> Transfer<C>>>(e).let { exceptionCase ->
            failure(exceptionCase.status, exceptionCase.error)
        }
    }

/**
 * Unfolds an Transfer action Quad<() -> Transfer<A>, () -> Transfer<B>, () -> Transfer<C>> from it's data separately.
 * When one transaction is failing the whole unfold operation will fail.
 * In this case the status of the transfer is been provided within the lambda.
 *
 * Quad<() -> Transfer<A>, () -> Transfer<B>, () -> Transfer<C>>.unfold (
 *     success = { firstData, secondData, thirdData ->
 *         firstData.doSomethingWith(secondData)
 *             .whichInvolves(thirdData)
 *             .inCaseOfSuccess()
 *     },
 *     failure = { status, error ->
 *     when(status){
 *         GENERAL_ERROR -> doSomethingWith(error)
 *         NETWORK_ERROR -> doSomethingElseWith(error)
 *         else -> ...
 *     })
 *
 * @param A First receive type of the function
 * @param B Second receive type of the function
 * @param C Third receive type of the function
 * @param success Defines what to do in case of a succeeding transfer action
 * @param failure Defines what to do in case of a failing transfer action
 * @return Returns Unit in every case
 */
suspend fun <A, B, C, D> Quad<suspend () -> Transfer<A>, suspend () -> Transfer<B>, suspend () -> Transfer<C>, suspend () -> Transfer<D>>.unfoldWithStatus(
    success: suspend (firstData: A, secondData: B, thirdData: C, fourthData: D) -> Unit = { _, _, _, _ -> },
    failure: suspend (status: TransferStatus, error: TransferError) -> Unit = { _, _ -> },
): Unit =
    try {
        coroutineScope {
            Quad(
                async { first() }.await(),
                async { second() }.await(),
                async { third() }.await(),
                async { fourth() }.await(),
            )
                .letCheckNull { resultA: Transfer<A>, resultB: Transfer<B>, resultC: Transfer<C>, resultD: Transfer<D> ->
                    when {
                        resultA is Transfer.Failure -> failure(resultA.status, resultA.error)
                        resultB is Transfer.Failure -> failure(resultB.status, resultB.error)
                        resultC is Transfer.Failure -> failure(resultC.status, resultC.error)
                        resultD is Transfer.Failure -> failure(resultC.status, resultD.error)
                        resultA is Transfer.Success && resultB is Transfer.Success && resultC is Transfer.Success && resultD is Transfer.Success ->
                            success(resultA.data, resultB.data, resultC.data, resultD.data)
                    }
                } ?: failure(TransferStatus.GENERAL_ERROR, TransferError("Failure happened while unfolding transfer"))
        }
    } catch (e: Exception) {
        Transfer.fromException<Triple<suspend () -> Transfer<A>, suspend () -> Transfer<B>, suspend () -> Transfer<C>>>(e).let { exceptionCase ->
            failure(exceptionCase.status, exceptionCase.error)
        }
    }
