package com.juancoob.kahootchallenge.fakes

import com.juancoob.domain.Choice
import com.juancoob.domain.Question
import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.server.RemoteService
import com.juancoob.kahootchallenge.data.server.models.Choice as ServerChoice
import com.juancoob.kahootchallenge.data.server.models.Question as ServerQuestion
import com.juancoob.kahootchallenge.data.server.models.Quiz as ServerQuiz

class FakeRemoteService(private val quiz: Quiz) : RemoteService {

    override suspend fun getQuiz(): ServerQuiz = quiz.fromLocalModel()
}

fun Quiz.fromLocalModel() = ServerQuiz(
    uuid = uuid,
    quizType = quizType,
    title = title,
    description = description,
    questions = questions.map { it.fromLocalModel() }
)

fun Question.fromLocalModel() = ServerQuestion(
    type = type,
    image = image,
    question = question,
    choices = choices.map { it.fromLocalModel() },
    pointsMultiplier = pointsMultiplier,
    time = time
)

fun Choice.fromLocalModel() = ServerChoice(
    answer = text,
    correct = isCorrect
)
