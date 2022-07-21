package com.juancoob.kahootchallenge.data.server

import arrow.core.Either
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.ui.common.wrapBlockWithIdlingResources
import javax.inject.Inject

class WrapRemoteDataSourceWithIdlingResources @Inject constructor(
    private val remoteDataSourceImpl: RemoteDataSourceImpl
) : RemoteDataSource {

    override suspend fun requestQuiz(): Either<ErrorRetrieved, Quiz> =
        wrapBlockWithIdlingResources {
            remoteDataSourceImpl.requestQuiz()
        }
}
