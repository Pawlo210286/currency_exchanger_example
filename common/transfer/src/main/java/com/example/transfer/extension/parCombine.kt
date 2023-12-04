package com.example.transfer.extension

import com.example.multifunctions.models.Hexa
import com.example.multifunctions.models.Penta
import com.example.multifunctions.models.Quad
import com.example.transfer.coroutines.runAsync
import com.example.transfer.model.Transfer
import com.example.transfer.tuples.tupled
import com.example.transfer.types.SuspendTransferF

suspend fun <A, B> parCombine(
    fa: SuspendTransferF<A>,
    fb: SuspendTransferF<B>
): Transfer<Pair<A, B>> =
    tupled(fa, fb).runAsync().combine()

suspend fun <A, B, C> parCombine(
    fa: SuspendTransferF<A>,
    fb: SuspendTransferF<B>,
    fc: SuspendTransferF<C>
): Transfer<Triple<A, B, C>> =
    tupled(fa, fb, fc).runAsync().combine()

suspend fun <A, B, C, D> parCombine(
    fa: SuspendTransferF<A>,
    fb: SuspendTransferF<B>,
    fc: SuspendTransferF<C>,
    fd: SuspendTransferF<D>,
): Transfer<Quad<A, B, C, D>> = tupled(fa, fb, fc, fd).runAsync().combine()

suspend fun <A, B, C, D, E> parCombine(
    fa: SuspendTransferF<A>,
    fb: SuspendTransferF<B>,
    fc: SuspendTransferF<C>,
    fd: SuspendTransferF<D>,
    fe: SuspendTransferF<E>,
): Transfer<Penta<A, B, C, D, E>> = tupled(fa, fb, fc, fd, fe).runAsync().combine()

suspend fun <A, B, C, D, E, F> parCombine(
    fa: SuspendTransferF<A>,
    fb: SuspendTransferF<B>,
    fc: SuspendTransferF<C>,
    fd: SuspendTransferF<D>,
    fe: SuspendTransferF<E>,
    ff: SuspendTransferF<F>,
): Transfer<Hexa<A, B, C, D, E, F>> = tupled(fa, fb, fc, fd, fe, ff).runAsync().combine()
