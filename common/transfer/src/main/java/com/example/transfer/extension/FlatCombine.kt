package com.example.transfer.extension

import com.example.multifunctions.letCheckNull
import com.example.transfer.model.Transfer

private const val FAILURE_MESSAGE = "Failure happened while combining transforms"

/**
 * Combines two and a nested transfer into one
 *
 * Pair(Transfer<A>, Transfer<B>).flatCombine { firstData, secondData ->
 *     Transfer.fromAction {
 *         firstData + secondData
 *     }
 * }
 *
 * @param Transfer<R> Return type of the function
 * @param block Defines the transformation from (A, B) to R within a lambda
 */
suspend fun <A, B, R> Pair<Transfer<A>, Transfer<B>>.flatCombine(
    block: suspend (firstData: A, secondData: B) -> Transfer<R>,
): Transfer<R> =
    Pair(first, second)
        .letCheckNull { firstTransfer, secondTransfer ->
            try {
                when {
                    firstTransfer is Transfer.Failure -> Transfer.Failure(firstTransfer.status, firstTransfer.error)
                    secondTransfer is Transfer.Failure -> Transfer.Failure(secondTransfer.status, secondTransfer.error)
                    firstTransfer is Transfer.Success && secondTransfer is Transfer.Success ->
                        block(firstTransfer.data, secondTransfer.data)
                    else -> Transfer.generalError(message = "Error happened while combining transforms")
                }
            } catch (e: Exception) {
                Transfer.fromException(e)
            }
        } ?: Transfer.generalError(FAILURE_MESSAGE)

/**
 * Combines three and a nested transfer into one transfer
 *
 * Pair(Transfer<A>, Transfer<B>, Transfer<C>).flatCombine { firstData, secondData, thirdData ->
 *     Transfer.fromAction {
 *         firstData + secondData + thirdData
 *     }
 * }
 *
 * @param Transfer<R> Return type of the function
 * @param block Defines the transformation from (A, B, C) to R within a lambda
 */
suspend fun <A, B, C, R> Triple<Transfer<A>, Transfer<B>, Transfer<C>>.flatCombine(
    block: suspend (firstData: A, secondData: B, thirdData: C) -> Transfer<R>,
): Transfer<R> =
    Triple(first, second, third)
        .letCheckNull { firstTransfer, secondTransfer, thirdTransfer ->
            try {
                when {
                    firstTransfer is Transfer.Failure -> Transfer.Failure(firstTransfer.status, firstTransfer.error)
                    secondTransfer is Transfer.Failure -> Transfer.Failure(secondTransfer.status, secondTransfer.error)
                    thirdTransfer is Transfer.Failure -> Transfer.Failure(thirdTransfer.status, thirdTransfer.error)
                    firstTransfer is Transfer.Success && secondTransfer is Transfer.Success && thirdTransfer is Transfer.Success ->
                        block(firstTransfer.data, secondTransfer.data, thirdTransfer.data)
                    else -> Transfer.generalError(message = "Error happened while combining transforms")
                }
            } catch (e: Exception) {
                Transfer.fromException(e)
            }
        } ?: Transfer.generalError(FAILURE_MESSAGE)
