package com.example.transfer.extension

import com.example.transfer.model.Transfer

/**
 * Transforms the data body of the transfer from T to Transfer<R>
 *
 * Transfer<T>.flatTransform { data ->
 *     firstData.doSomething()
 * }
 *
 * @param T Receive type of the function
 * @param Transfer<R> Return type of the function
 * @param transform Defines the transformation from T to R within a lambda
 * @return Transfer<R> Returns a transfer with type Transfer<R>
 */
inline fun <T, R> Transfer<T>.flatTransform(transform: (data: T) -> Transfer<R>): Transfer<R> =
    when (this) {
        is Transfer.Success -> try {
            transform(data)
        } catch (e: Exception) {
            Transfer.fromException(e)
        }
        is Transfer.Failure -> Transfer.Failure(status = status, error = error)
    }

fun <T> Transfer<Transfer<T>>.flatten(): Transfer<T> =
    flatTransform { it }

/**
 * Transforms the data body of an transform Pair<A, B> separately.
 *
 * Transfer<Pair<A, B>>.flatTransform { firstData, secondData ->
 *     firstData.doSomethingWith(secondData)
 * }
 *
 * @param A First receive type of the function
 * @param B Second receive type of the function
 * @param R Return type of the function
 * @param transform Defines the transformation from Pair<A, B> to R within a lambda
 * @return Transfer<R> Returns a transfer with type <R>
 */
inline fun <A, B, R> Transfer<Pair<A, B>>.flatTransform(transform: (firstData: A, secondData: B) -> Transfer<R>): Transfer<R> =
    when (this) {
        is Transfer.Success -> try {
            transform(data.first, data.second)
        } catch (e: Exception) {
            Transfer.fromException(e)
        }
        is Transfer.Failure -> Transfer.Failure(status = status, error = error)
    }

/**
 * FlatTransforms the data body of an transform Triple<A, B, C> separately.
 *
 * Transfer<Triple<A, B, C>>.flatTransform { firstData, secondData, thirdData ->
 *     firstData
 *         .doSomethingWith(secondData)
 *         .whichInvolves(thirdData)
 * }
 *
 * @param A First receive type of the function
 * @param B Second receive type of the function
 * @param C Third receive type of the function
 * @param R Return type of the function
 * @param transform Defines the transformation from Triple<A, B, C> to R within a lambda
 * @return Transfer<R> Returns a transfer with type <R>
 */
inline fun <A, B, C, R> Transfer<Triple<A, B, C>>.flatTransform(transform: (firstData: A, secondData: B, thirdData: C) -> Transfer<R>): Transfer<R> =
    when (this) {
        is Transfer.Success -> try {
            transform(data.first, data.second, data.third)
        } catch (e: Exception) {
            Transfer.fromException(e)
        }
        is Transfer.Failure -> Transfer.Failure(status = status, error = error)
    }
