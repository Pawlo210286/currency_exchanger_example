package com.example.transfer.coroutines

import com.example.multifunctions.models.Hexa
import com.example.multifunctions.models.Penta
import com.example.multifunctions.models.Quad
import com.example.transfer.tuples.tupled
import com.example.transfer.types.SuspendF
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun <A, B> Pair<SuspendF<A>, SuspendF<B>>.runAsync(): Pair<A, B> = coroutineScope {
    tupled(async { first() }, async { second() })
        .let { (def1, def2) -> tupled(def1.await(), def2.await()) }
}

suspend fun <A, B, C> Triple<SuspendF<A>, SuspendF<B>, SuspendF<C>>.runAsync(): Triple<A, B, C> =
    coroutineScope {
        tupled(
            async { first() },
            async { second() },
            async { third() }).let { (def1, def2, def3) ->
            tupled(
                def1.await(),
                def2.await(),
                def3.await()
            )
        }
    }

suspend fun <A, B, C, D> Quad<SuspendF<A>, SuspendF<B>, SuspendF<C>, SuspendF<D>>.runAsync(): Quad<A, B, C, D> =
    coroutineScope {
        tupled(async { first() }, async { second() }, async { third() }, async { fourth() })
            .let { (def1, def2, def3, def4) ->
                tupled(
                    def1.await(),
                    def2.await(),
                    def3.await(),
                    def4.await()
                )
            }
    }

suspend fun <A, B, C, D, E> Penta<SuspendF<A>, SuspendF<B>, SuspendF<C>, SuspendF<D>, SuspendF<E>>.runAsync(): Penta<A, B, C, D, E> =
    coroutineScope {
        tupled(
            async { first() },
            async { second() },
            async { third() },
            async { fourth() },
            async { fifth() })
            .let { (def1, def2, def3, def4, def5) ->
                tupled(
                    def1.await(),
                    def2.await(),
                    def3.await(),
                    def4.await(),
                    def5.await()
                )
            }
    }

suspend fun <A, B, C, D, E, F> Hexa<SuspendF<A>, SuspendF<B>, SuspendF<C>, SuspendF<D>, SuspendF<E>, SuspendF<F>>.runAsync(): Hexa<A, B, C, D, E, F> =
    coroutineScope {
        tupled(
            async { first() },
            async { second() },
            async { third() },
            async { fourth() },
            async { fifth() },
            async { sixth() })
            .let { (def1, def2, def3, def4, def5, def6) ->
                tupled(
                    def1.await(),
                    def2.await(),
                    def3.await(),
                    def4.await(),
                    def5.await(),
                    def6.await()
                )
            }
    }
