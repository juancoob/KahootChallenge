package com.juancoob.data

import com.juancoob.data.datasource.LocalDataSource
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Quiz
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    suspend fun requestQuiz(): ErrorRetrieved? =
        if (localDataSource.isEmpty()) {
            remoteDataSource.requestQuiz().fold(ifLeft = { it }) {
                localDataSource.insertQuizData(it)
            }
        } else {
            null
        }

    fun getQuiz(): Flow<Quiz> = localDataSource.getQuiz()
}
