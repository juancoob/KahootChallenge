package com.juancoob.kahootchallenge.data.server

import com.juancoob.domain.Choice
import com.juancoob.domain.Question
import com.juancoob.domain.Quiz
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject
import com.juancoob.kahootchallenge.data.server.models.Choice as ServerChoice
import com.juancoob.kahootchallenge.data.server.models.Question as ServerQuestion
import com.juancoob.kahootchallenge.data.server.models.Quiz as ServerQuiz

class RemoteLocalMapper @Inject constructor() {

    fun ServerQuiz.toLocalModel() = Quiz(
        uuid = uuid,
        quizType = quizType,
        title = title,
        description = description,
        questions = questions.map { it.toLocalModel() }
    )

    private fun ServerQuestion.toLocalModel() = Question(
        type = type,
        image = image,
        question = question,
        choices = choices.map { it.toLocalModel() },
        pointsMultiplier = pointsMultiplier,
        time = time
    )

    private fun ServerChoice.toLocalModel() = Choice(
        text = answer,
        isCorrect = correct,
        showAnswer = false,
        isSelected = false
    )

    @TestOnly
    fun Quiz.fromLocalModel() = ServerQuiz(
        uuid = uuid,
        quizType = quizType,
        title = title,
        description = description,
        questions = questions.map { it.fromLocalModel() }
    )

    private fun Question.fromLocalModel() = ServerQuestion(
        type = type,
        image = image,
        question = question,
        choices = choices.map { it.fromLocalModel() },
        pointsMultiplier = pointsMultiplier,
        time = time
    )

    private fun Choice.fromLocalModel() = ServerChoice(
        answer = text,
        correct = isCorrect
    )
}
