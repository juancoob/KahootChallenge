package com.juancoob.data.datasource

import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Quiz
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun isEmpty(): Boolean
    suspend fun insertQuizData(quiz: Quiz): ErrorRetrieved?
    fun getQuiz(): Flow<Quiz>
}
