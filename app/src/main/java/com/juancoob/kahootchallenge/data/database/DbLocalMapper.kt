package com.juancoob.kahootchallenge.data.database

import com.juancoob.domain.Choice
import com.juancoob.domain.Question
import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.database.models.QuestionWithChoices
import com.juancoob.kahootchallenge.data.database.models.QuizWithQuestions
import javax.inject.Inject
import com.juancoob.kahootchallenge.data.database.models.Choice as DbChoice
import com.juancoob.kahootchallenge.data.database.models.Question as DbQuestion
import com.juancoob.kahootchallenge.data.database.models.Quiz as DbQuiz

class DbLocalMapper @Inject constructor() {

    fun Quiz.fromLocalModel(): DbQuiz = DbQuiz(
        uuid = uuid,
        title = title,
        description = description,
        quizType = quizType
    )

    fun Question.fromLocalModel(quizId: String, questionId: Int) = DbQuestion(
        id = questionId,
        quizId = quizId,
        type = type,
        image = image,
        question = question,
        pointsMultiplier = pointsMultiplier,
        time = time
    )

    fun Choice.fromLocalModel(questionId: Int, choiceId: Int) = DbChoice(
        choiceId = choiceId,
        questionId = questionId,
        answer = text,
        correct = isCorrect
    )

    fun QuizWithQuestions.toLocalModel() = quiz.run {
        Quiz(
            uuid = uuid,
            quizType = quizType,
            title = title,
            description = description,
            questions = questions.map { it.toLocalModel() }
        )
    }

    private fun QuestionWithChoices.toLocalModel() = question.run {
        Question(
            type = type,
            image = image,
            question = question,
            choices = choices.map { it.toLocalModel() },
            pointsMultiplier = pointsMultiplier,
            time = time
        )
    }

    private fun DbChoice.toLocalModel() = Choice(
        text = answer,
        isCorrect = correct,
        showAnswer = false,
        isSelected = false
    )
}
