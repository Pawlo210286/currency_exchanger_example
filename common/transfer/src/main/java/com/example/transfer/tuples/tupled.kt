package com.example.transfer.tuples

import com.example.multifunctions.models.Hexa
import com.example.multifunctions.models.Penta
import com.example.multifunctions.models.Quad

fun <A, B> tupled(a: A, b: B): Pair<A, B> = Pair(a, b)
fun <A, B, C> tupled(a: A, b: B, c: C): Triple<A, B, C> = Triple(a, b, c)
fun <A, B, C, D> tupled(a: A, b: B, c: C, d: D): Quad<A, B, C, D> = Quad(a, b, c, d)
fun <A, B, C, D, E> tupled(a: A, b: B, c: C, d: D, e: E): Penta<A, B, C, D, E> = Penta(a, b, c, d, e)
fun <A, B, C, D, E, F> tupled(a: A, b: B, c: C, d: D, e: E, f: F): Hexa<A, B, C, D, E, F> = Hexa(a, b, c, d, e, f)

