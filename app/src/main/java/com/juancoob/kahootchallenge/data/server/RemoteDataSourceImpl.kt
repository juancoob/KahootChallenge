package com.juancoob.kahootchallenge.data.server

import arrow.core.Either
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.domain.Choice
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Question
import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.tryCall
import com.juancoob.kahootchallenge.data.server.models.Choice as ServerChoice
import com.juancoob.kahootchallenge.data.server.models.Question as ServerQuestion
import com.juancoob.kahootchallenge.data.server.models.Quiz as ServerQuiz

class RemoteDataSourceImpl(
    private val service: RemoteService
) : RemoteDataSource {
    override suspend fun requestQuiz(): Either<ErrorRetrieved, Quiz> = tryCall {
        service.getQuiz().toLocalModel()
    }
}

fun ServerQuiz.toLocalModel() = Quiz(
    quizType = quizType,
    title = title,
    description = description,
    questions = questions.map { it.toLocalModel() }
)

fun ServerQuestion.toLocalModel() = Question(
    type = type,
    image = image,
    question = question,
    choices = choices.map { it.toLocalModel() },
    hasPoints = hasPoints,
    pointsMultiplier = pointsMultiplier,
    time = time
)

fun ServerChoice.toLocalModel() = Choice(
    text = answer,
    isCorrect = correct,
    showAnswer = false,
    isSelected = false
)
