package com.example.multifunctions

import com.example.multifunctions.models.Hepta
import com.example.multifunctions.models.Hexa
import com.example.multifunctions.models.Penta
import com.example.multifunctions.models.Quad

/**
 * Returns a list containing only the non-null results of applying the given [transform] function
 * to each element in the original collection.
 */
public inline infix fun <A, B, R : Any> Iterable<Pair<A?, B?>>.mapNotNull(transform: (A?, B?) -> R?): List<R> =
    mapNotNull { (first, second) ->
        when {
            first == null && second == null -> null
            else -> transform(first, second)
        }
    }

/**
 * Returns a list containing only the non-null results of applying the given [transform] function
 * to each element in the original collection.
 */
public inline infix fun <A, B, C, R : Any> Iterable<Triple<A?, B?, C?>>.mapNotNull(transform: (A?, B?, C?) -> R?): List<R> =
    mapNotNull { (first, second, third) ->
        when {
            first == null && second == null && third == null -> null
            else -> transform(first, second, third)
        }
    }

/**
 * Returns a list containing only the non-null results of applying the given [transform] function
 * to each element in the original collection.
 */
public inline infix fun <A, B, C, D, R : Any> Iterable<Quad<A?, B?, C?, D?>>.mapNotNull(transform: (A?, B?, C?, D?) -> R?): List<R> =
    mapNotNull { (first, second, third, fourth) ->
        when {
            first == null && second == null && third == null && fourth == null -> null
            else -> transform(first, second, third, fourth)
        }
    }

/**
 * Returns a list containing only the non-null results of applying the given [transform] function
 * to each element in the original collection.
 */
public inline infix fun <A, B, C, D, E, R : Any> Iterable<Penta<A?, B?, C?, D?, E?>>.mapNotNull(
    transform: (A?, B?, C?, D?, E?) -> R?
): List<R> =
    mapNotNull { (first, second, third, fourth, fifth) ->
        when {
            first == null && second == null && third == null && fourth == null && fifth == null -> null
            else -> transform(first, second, third, fourth, fifth)
        }
    }

/**
 * Returns a list containing only the non-null results of applying the given [transform] function
 * to each element in the original collection.
 */
public inline infix fun <A, B, C, D, E, F, R : Any> Iterable<Hexa<A?, B?, C?, D?, E?, F?>>.mapNotNull(
    transform: (A?, B?, C?, D?, E?, F?) -> R?
): List<R> =
    mapNotNull { (first, second, third, fourth, fifth, sixth) ->
        when {
            first == null && second == null && third == null && fourth == null && fifth == null && sixth == null -> null
            else -> transform(first, second, third, fourth, fifth, sixth)
        }
    }

/**
 * Returns a list containing only the non-null results of applying the given [transform] function
 * to each element in the original collection.
 */
public inline infix fun <A, B, C, D, E, F, G, R : Any> Iterable<Hepta<A?, B?, C?, D?, E?, F?, G?>>.mapNotNull(
    transform: (A?, B?, C?, D?, E?, F?, G?) -> R?
): List<R> =
    mapNotNull { (first, second, third, fourth, fifth, sixth, seventh) ->
        when {
            first == null && second == null && third == null && fourth == null && fifth == null && sixth == null && seventh == null -> null
            else -> transform(first, second, third, fourth, fifth, sixth, seventh)
        }
    }
