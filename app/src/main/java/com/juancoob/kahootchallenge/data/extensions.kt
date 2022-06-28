package com.juancoob.kahootchallenge.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.juancoob.domain.ErrorRetrieved
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> tryCall(action: suspend () -> T): Either<ErrorRetrieved, T> = try {
    action().right()
} catch (exception: Exception) {
    exception.toErrorRetrieved().left()
}

fun Throwable.toErrorRetrieved(): ErrorRetrieved = when (this) {
    is IOException -> ErrorRetrieved.Connectivity
    is HttpException -> ErrorRetrieved.Server(code())
    else -> ErrorRetrieved.Unknown(message ?: "")
}
