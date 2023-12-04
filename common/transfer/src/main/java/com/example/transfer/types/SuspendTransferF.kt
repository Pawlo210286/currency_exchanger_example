package com.example.transfer.types

import com.example.transfer.model.Transfer

typealias SuspendTransferF<R> = suspend () -> Transfer<R>