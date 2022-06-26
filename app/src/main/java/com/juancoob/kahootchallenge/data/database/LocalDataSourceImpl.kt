package com.juancoob.kahootchallenge.data.database

import com.juancoob.data.datasource.LocalDataSource
import com.juancoob.domain.Choice
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Question
import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.database.models.QuestionWithChoices
import com.juancoob.kahootchallenge.data.database.models.QuizWithQuestions
import com.juancoob.kahootchallenge.data.tryCall
import javax.inject.Inject
import com.juancoob.kahootchallenge.data.database.models.Choice as DbChoice
import com.juancoob.kahootchallenge.data.database.models.Question as DbQuestion
import com.juancoob.kahootchallenge.data.database.models.Quiz as DbQuiz

class LocalDataSourceImpl @Inject constructor(
    private val quizDao: QuizDao
) : LocalDataSource {
    override suspend fun isEmpty(): Boolean =
        quizDao.getQuizCount() == 0

    override suspend fun insertQuizData(quiz: Quiz): ErrorRetrieved? = tryCall {
        quizDao.insertQuiz(quiz.fromLocalModel())

        for (question in quiz.questions) {
            val questionIndex = quiz.questions.indexOf(question)
            quizDao.insertQuestion(question.fromLocalModel(quiz.uuid, questionIndex))

            for (choice in question.choices) {
                val choiceIndex = question.choices.indexOf(choice)
                quizDao.insertChoice(choice.fromLocalModel(questionIndex, choiceIndex))
            }
        }
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override suspend fun getQuiz(): Quiz =
        quizDao.getQuiz().toLocalModel()
}

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
    hasPoints = hasPoints,
    pointsMultiplier = pointsMultiplier,
    time = time
)

fun Choice.fromLocalModel(questionId: Int, choiceId: Int) = DbChoice(
    id = choiceId,
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

fun QuestionWithChoices.toLocalModel() = question.run {
    Question(
        type = type,
        image = image,
        question = question,
        choices = choices.map { it.toLocalModel() },
        hasPoints = hasPoints,
        pointsMultiplier = pointsMultiplier,
        time = time
    )
}

fun DbChoice.toLocalModel() = Choice(
    text = answer,
    isCorrect = correct,
    showAnswer = false,
    isSelected = false
)
