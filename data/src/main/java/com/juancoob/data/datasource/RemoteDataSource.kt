package com.juancoob.data.datasource

import arrow.core.Either
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Quiz

interface RemoteDataSource {
    suspend fun requestQuiz(): Either<ErrorRetrieved, Quiz>
}
