@file:Suppress("RedundantAsync", "UNCHECKED_CAST")

package com.example.transfer.extension

import com.example.multifunctions.letCheckNull
import com.example.multifunctions.models.Hexa
import com.example.multifunctions.models.Penta
import com.example.multifunctions.models.Quad
import com.example.transfer.model.Transfer
import com.example.transfer.model.TransferError
import com.example.transfer.tuples.tupled
import com.example.transfer.model.TransferStatus

private const val FAILURE_MESSAGE = "Failure happened while combining transforms"

/**
 * Combines two transfers into one transfer
 *
 * Pair(Transfer<A>, Transfer<B>).combine()
 *
 * @return Transfer<Pair<A, B>> Return type of the function
 */
suspend fun <A, B> Pair<Transfer<A>, Transfer<B>>.combine(): Transfer<Pair<A, B>> =
    Pair(first, second)
        .letCheckNull { firstTransfer, secondTransfer ->
            try {
                when {
                    firstTransfer is Transfer.Failure -> Transfer.Failure(
                        firstTransfer.status,
                        firstTransfer.error
                    )

                    secondTransfer is Transfer.Failure -> Transfer.Failure(
                        secondTransfer.status,
                        secondTransfer.error
                    )

                    firstTransfer is Transfer.Success && secondTransfer is Transfer.Success ->
                        Transfer.success(Pair(firstTransfer.data, secondTransfer.data))

                    else -> Transfer.generalError(FAILURE_MESSAGE)
                }
            } catch (e: Exception) {
                Transfer.fromException(e)
            }
        } ?: Transfer.generalError(FAILURE_MESSAGE)

/**
 * Combines three transfers into one transfer
 *
 * Pair(Transfer<A>, Transfer<B>, Transfer<C>).combine()
 *
 * @return Transfer<Triple<A, B, C>> Return type of the function
 */
suspend fun <A, B, C> Triple<Transfer<A>, Transfer<B>, Transfer<C>>.combine(): Transfer<Triple<A, B, C>> =
    Triple(first, second, third)
        .letCheckNull { firstTransfer, secondTransfer, thirdTransfer ->
            try {
                when {
                    firstTransfer is Transfer.Failure -> Transfer.Failure(
                        firstTransfer.status,
                        firstTransfer.error
                    )

                    secondTransfer is Transfer.Failure -> Transfer.Failure(
                        secondTransfer.status,
                        secondTransfer.error
                    )

                    thirdTransfer is Transfer.Failure -> Transfer.Failure(
                        thirdTransfer.status,
                        thirdTransfer.error
                    )

                    firstTransfer is Transfer.Success && secondTransfer is Transfer.Success && thirdTransfer is Transfer.Success ->
                        Transfer.success(
                            Triple(
                                firstTransfer.data,
                                secondTransfer.data,
                                thirdTransfer.data
                            )
                        )

                    else -> Transfer.generalError(FAILURE_MESSAGE)
                }
            } catch (e: Exception) {
                Transfer.fromException(e)
            }
        } ?: Transfer.generalError(FAILURE_MESSAGE)

/**
 * Combines four transfers into one transfer
 *
 * Quad(Transfer<A>, Transfer<B>, Transfer<C>, Transfer<D>).combine()
 *
 * @return Transfer<Triple<A, B, C, D>> Return type of the function
 */
suspend fun <A, B, C, D> Quad<Transfer<A>, Transfer<B>, Transfer<C>, Transfer<D>>.combine(): Transfer<Quad<A, B, C, D>> =
    Quad(first, second, third, fourth)
        .letCheckNull { firstTransfer, secondTransfer, thirdTransfer, fourthTransfer ->
            try {
                when {
                    firstTransfer is Transfer.Failure -> Transfer.Failure(
                        firstTransfer.status,
                        firstTransfer.error
                    )

                    secondTransfer is Transfer.Failure -> Transfer.Failure(
                        secondTransfer.status,
                        secondTransfer.error
                    )

                    thirdTransfer is Transfer.Failure -> Transfer.Failure(
                        thirdTransfer.status,
                        thirdTransfer.error
                    )

                    fourthTransfer is Transfer.Failure -> Transfer.Failure(
                        fourthTransfer.status,
                        fourthTransfer.error
                    )

                    firstTransfer is Transfer.Success
                            && secondTransfer is Transfer.Success
                            && thirdTransfer is Transfer.Success
                            && fourthTransfer is Transfer.Success
                    -> Transfer.success(
                        Quad(
                            firstTransfer.data,
                            secondTransfer.data,
                            thirdTransfer.data,
                            fourthTransfer.data
                        )
                    )

                    else -> Transfer.generalError(FAILURE_MESSAGE)
                }
            } catch (e: Exception) {
                Transfer.fromException(e)
            }
        } ?: Transfer.generalError(FAILURE_MESSAGE)

/**
 * Combines two transfers into one transfer
 *
 * Pair(Transfer<A>, Transfer<B>).combine { firstData, secondData ->
 *     Transfer.fromAction {
 *         firstData + secondData
 *     }
 * }
 *
 * @return Transfer<R> Return type of the function
 * @param block Defines the transformation from (A, B) to R within a lambda
 */
suspend fun <A, B, R> Pair<Transfer<A>, Transfer<B>>.combine(block: (firstData: A, secondData: B) -> R): Transfer<R> =
    Pair(first, second)
        .letCheckNull { firstTransfer, secondTransfer ->
            try {
                when {
                    firstTransfer is Transfer.Failure -> Transfer.Failure(
                        firstTransfer.status,
                        firstTransfer.error
                    )

                    secondTransfer is Transfer.Failure -> Transfer.Failure(
                        secondTransfer.status,
                        secondTransfer.error
                    )

                    firstTransfer is Transfer.Success && secondTransfer is Transfer.Success ->
                        Transfer.success(block(firstTransfer.data, secondTransfer.data))

                    else -> Transfer.generalError(FAILURE_MESSAGE)
                }
            } catch (e: Exception) {
                Transfer.fromException(e)
            }
        } ?: Transfer.generalError(FAILURE_MESSAGE)

/**
 * Combines three transfers into one transfer
 *
 * Triple(Transfer<A>, Transfer<B>, Transfer<C>).combine { firstData, secondData, thirdData ->
 *     Transfer.fromAction {
 *         firstData + secondData + thirdData
 *     }
 * }
 *
 * @return Transfer<R> Return type of the function
 * @param block Defines the transformation from (A, B, C) to R within a lambda
 */
suspend fun <A, B, C, R> Triple<Transfer<A>, Transfer<B>, Transfer<C>>.combine(
    block: (firstData: A, secondData: B, thirdData: C) -> R,
): Transfer<R> =
    Triple(first, second, third)
        .letCheckNull { firstTransfer, secondTransfer, thirdTransfer ->
            try {
                when {
                    firstTransfer is Transfer.Failure -> Transfer.Failure(
                        firstTransfer.status,
                        firstTransfer.error
                    )

                    secondTransfer is Transfer.Failure -> Transfer.Failure(
                        secondTransfer.status,
                        secondTransfer.error
                    )

                    thirdTransfer is Transfer.Failure -> Transfer.Failure(
                        thirdTransfer.status,
                        thirdTransfer.error
                    )

                    firstTransfer is Transfer.Success && secondTransfer is Transfer.Success && thirdTransfer is Transfer.Success ->
                        Transfer.success(
                            block(
                                firstTransfer.data,
                                secondTransfer.data,
                                thirdTransfer.data
                            )
                        )

                    else -> Transfer.generalError(FAILURE_MESSAGE)
                }
            } catch (e: Exception) {
                Transfer.fromException(e)
            }
        } ?: Transfer.generalError(FAILURE_MESSAGE)

/**
 * Combines four transfers into one transfer
 *
 * Quad(Transfer<A>, Transfer<B>, Transfer<C>).combine { firstData, secondData, thirdData ->
 *     Transfer.fromAction {
 *         firstData + secondData + thirdData
 *     }
 * }
 *
 * @return Transfer<R> Return type of the function
 * @param block Defines the transformation from (A, B, C) to R within a lambda
 */
suspend fun <A, B, C, D, R> Quad<Transfer<A>, Transfer<B>, Transfer<C>, Transfer<D>>.combine(
    block: (firstData: A, secondData: B, thirdData: C, fourthData: D) -> R,
): Transfer<R> =
    Quad(first, second, third, fourth)
        .letCheckNull { firstTransfer, secondTransfer, thirdTransfer, fourthTransfer ->
            try {
                when {
                    firstTransfer is Transfer.Failure -> Transfer.Failure(
                        firstTransfer.status,
                        firstTransfer.error
                    )

                    secondTransfer is Transfer.Failure -> Transfer.Failure(
                        secondTransfer.status,
                        secondTransfer.error
                    )

                    thirdTransfer is Transfer.Failure -> Transfer.Failure(
                        thirdTransfer.status,
                        thirdTransfer.error
                    )

                    fourthTransfer is Transfer.Failure -> Transfer.Failure(
                        fourthTransfer.status,
                        fourthTransfer.error
                    )

                    firstTransfer is Transfer.Success
                            && secondTransfer is Transfer.Success
                            && thirdTransfer is Transfer.Success
                            && fourthTransfer is Transfer.Success ->
                        Transfer.success(
                            block(
                                firstTransfer.data,
                                secondTransfer.data,
                                thirdTransfer.data,
                                fourthTransfer.data
                            )
                        )

                    else -> Transfer.generalError(FAILURE_MESSAGE)
                }
            } catch (e: Exception) {
                Transfer.fromException(e)
            }
        } ?: Transfer.generalError(FAILURE_MESSAGE)

/**
 * Combines five transfers into one transfer
 *
 * Quad(Transfer<A>, Transfer<B>, Transfer<C>).combine { firstData, secondData, thirdData ->
 *     Transfer.fromAction {
 *         firstData + secondData + thirdData
 *     }
 * }
 *
 * @return Transfer<R> Return type of the function
 * @param block Defines the transformation from (A, B, C) to R within a lambda
 */
suspend fun <A, B, C, D, E, R> Penta<Transfer<A>, Transfer<B>, Transfer<C>, Transfer<D>, Transfer<E>>.combine(
    block: (firstData: A, secondData: B, thirdData: C, fourthData: D, fifthData: E) -> R,
): Transfer<R> =
    Penta(first, second, third, fourth, fifth)
        .letCheckNull { firstTransfer, secondTransfer, thirdTransfer, fourthTransfer, fifthTransfer ->
            try {
                when {
                    firstTransfer is Transfer.Failure -> Transfer.Failure(
                        firstTransfer.status,
                        firstTransfer.error
                    )

                    secondTransfer is Transfer.Failure -> Transfer.Failure(
                        secondTransfer.status,
                        secondTransfer.error
                    )

                    thirdTransfer is Transfer.Failure -> Transfer.Failure(
                        thirdTransfer.status,
                        thirdTransfer.error
                    )

                    fourthTransfer is Transfer.Failure -> Transfer.Failure(
                        fourthTransfer.status,
                        fourthTransfer.error
                    )

                    fifthTransfer is Transfer.Failure -> Transfer.Failure(
                        fifthTransfer.status,
                        fifthTransfer.error
                    )

                    firstTransfer is Transfer.Success
                            && secondTransfer is Transfer.Success
                            && thirdTransfer is Transfer.Success
                            && fourthTransfer is Transfer.Success
                            && fifthTransfer is Transfer.Success ->
                        Transfer.success(
                            block(
                                firstTransfer.data,
                                secondTransfer.data,
                                thirdTransfer.data,
                                fourthTransfer.data,
                                fifthTransfer.data
                            )
                        )

                    else -> Transfer.generalError(FAILURE_MESSAGE)
                }
            } catch (e: Exception) {
                Transfer.fromException(e)
            }
        } ?: Transfer.generalError(FAILURE_MESSAGE)


/**
 * Combine five Transfers into one Transfer
 *
 * @return Transfer<Penta<A, B, C, D, E>>
 */
fun <A, B, C, D, E> Penta<Transfer<A>, Transfer<B>, Transfer<C>, Transfer<D>, Transfer<E>>.combine(): Transfer<Penta<A, B, C, D, E>> =
    let { (firstTransfer, secondTransfer, thirdTransfer, fourthTransfer, fifthTransfer) ->
        when {
            firstTransfer is Transfer.Failure -> firstTransfer as Transfer<Penta<A, B, C, D, E>>
            secondTransfer is Transfer.Failure -> secondTransfer as Transfer<Penta<A, B, C, D, E>>
            thirdTransfer is Transfer.Failure -> thirdTransfer as Transfer<Penta<A, B, C, D, E>>
            fourthTransfer is Transfer.Failure -> fourthTransfer as Transfer<Penta<A, B, C, D, E>>
            fifthTransfer is Transfer.Failure -> fifthTransfer as Transfer<Penta<A, B, C, D, E>>

            firstTransfer is Transfer.Success
                    && secondTransfer is Transfer.Success
                    && thirdTransfer is Transfer.Success
                    && fourthTransfer is Transfer.Success
                    && fifthTransfer is Transfer.Success -> {

                Transfer.success(
                    tupled(
                        firstTransfer.data,
                        secondTransfer.data,
                        thirdTransfer.data,
                        fourthTransfer.data,
                        fifthTransfer.data
                    )
                )
            }

            else -> Transfer.Failure(
                TransferStatus.GENERAL_ERROR,
                TransferError.fromThrowable(IllegalStateException("Illegal State in combine"))
            )
        }
    }


/**
 * Combine six Transfers into one Transfer
 *
 * @return Transfer<Hexa<A, B, C, D, E>>
 */
fun <A, B, C, D, E, F> Hexa<Transfer<A>, Transfer<B>, Transfer<C>, Transfer<D>, Transfer<E>, Transfer<F>>.combine(): Transfer<Hexa<A, B, C, D, E, F>> =
    let { (firstTransfer, secondTransfer, thirdTransfer, fourthTransfer, fifthTransfer, sixthTransfer) ->
        when {
            firstTransfer is Transfer.Failure -> firstTransfer as Transfer<Hexa<A, B, C, D, E, F>>
            secondTransfer is Transfer.Failure -> secondTransfer as Transfer<Hexa<A, B, C, D, E, F>>
            thirdTransfer is Transfer.Failure -> thirdTransfer as Transfer<Hexa<A, B, C, D, E, F>>
            fourthTransfer is Transfer.Failure -> fourthTransfer as Transfer<Hexa<A, B, C, D, E, F>>
            fifthTransfer is Transfer.Failure -> fifthTransfer as Transfer<Hexa<A, B, C, D, E, F>>
            sixthTransfer is Transfer.Failure -> sixthTransfer as Transfer<Hexa<A, B, C, D, E, F>>

            firstTransfer is Transfer.Success
                    && secondTransfer is Transfer.Success
                    && thirdTransfer is Transfer.Success
                    && fourthTransfer is Transfer.Success
                    && fifthTransfer is Transfer.Success
                    && sixthTransfer is Transfer.Success -> {

                Transfer.success(
                    tupled(
                        firstTransfer.data,
                        secondTransfer.data,
                        thirdTransfer.data,
                        fourthTransfer.data,
                        fifthTransfer.data,
                        sixthTransfer.data
                    )
                )
            }

            else -> Transfer.Failure(
                TransferStatus.GENERAL_ERROR,
                TransferError.fromThrowable(IllegalStateException("Illegal State in combine"))
            )
        }
    }
