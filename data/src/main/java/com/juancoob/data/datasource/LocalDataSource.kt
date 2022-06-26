package com.juancoob.data.datasource

import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Quiz

interface LocalDataSource {
    suspend fun isEmpty(): Boolean
    suspend fun insertQuizData(quiz: Quiz): ErrorRetrieved?
    suspend fun getQuiz(): Quiz
}
