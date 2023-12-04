package com.example.transfer.http.extension

import com.example.transfer.http.HttpCode.CLIENT_ERROR
import com.example.transfer.http.HttpCode.CONTINUE
import com.example.transfer.http.HttpCode.REDIRECT
import com.example.transfer.http.HttpCode.SERVER_ERROR
import com.example.transfer.http.HttpCode.SUCCESS
import com.example.transfer.http.HttpCode.UNKNOWN

fun Int.isContinue(): Boolean = this in CONTINUE until SUCCESS

fun Int.isSuccess(): Boolean = this in SUCCESS until REDIRECT

fun Int.isRedirect(): Boolean = this in REDIRECT until CLIENT_ERROR

fun Int.isClientError(): Boolean = this in CLIENT_ERROR until SERVER_ERROR

fun Int.isServerError(): Boolean = this in SERVER_ERROR until UNKNOWN

fun Int.isUnknown(): Boolean = this < CONTINUE || this >= UNKNOWN
