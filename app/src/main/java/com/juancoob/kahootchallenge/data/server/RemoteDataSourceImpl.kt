package com.juancoob.kahootchallenge.data.server

import arrow.core.Either
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.tryCall
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val service: RemoteService,
    private val remoteLocalMapper: RemoteLocalMapper
) : RemoteDataSource {

    override suspend fun requestQuiz(): Either<ErrorRetrieved, Quiz> = tryCall {
        with(remoteLocalMapper) { service.getQuiz().toLocalModel() }
    }
}
