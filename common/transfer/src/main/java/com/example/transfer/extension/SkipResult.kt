package com.example.transfer.extension

import com.example.transfer.model.Transfer

fun <T> Transfer<T>.skipResult(): Transfer<Unit> = transform { }
