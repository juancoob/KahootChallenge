package com.juancoob.kahootchallenge.data.local

import com.juancoob.data.datasource.LocalDataSource
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.database.LocalDataSourceImpl
import com.juancoob.kahootchallenge.ui.common.wrapBlockWithIdlingResources
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DecoratedLocalDataSource @Inject constructor(
    private val localDataSourceImpl: LocalDataSourceImpl
) : LocalDataSource {

    override suspend fun isEmpty(): Boolean = wrapBlockWithIdlingResources {
        localDataSourceImpl.isEmpty()
    }

    override suspend fun insertQuizData(quiz: Quiz): ErrorRetrieved? =
        wrapBlockWithIdlingResources {
            localDataSourceImpl.insertQuizData(quiz)
        }

    override fun getQuiz(): Flow<Quiz> = wrapBlockWithIdlingResources {
        localDataSourceImpl.getQuiz()
    }
}
