package com.juancoob.kahootchallenge.data.database

import com.juancoob.data.datasource.LocalDataSource
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.tryCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val quizDao: QuizDao,
    private val dbLocalMapper: DbLocalMapper
) : LocalDataSource {

    private var questionIndex: Int = -1
    private var choiceIndex: Int = -1

    override suspend fun isEmpty(): Boolean =
        quizDao.getQuizCount() == 0

    override suspend fun insertQuizData(quiz: Quiz): ErrorRetrieved? = tryCall {
        quizDao.insertQuiz(with(dbLocalMapper) { quiz.fromLocalModel() })

        for (question in quiz.questions) {
            questionIndex = quiz.questions.indexOf(question)
            quizDao.insertQuestion(
                with(dbLocalMapper) {
                    question.fromLocalModel(
                        quiz.uuid,
                        questionIndex
                    )
                }
            )

            for (choice in question.choices) {
                choiceIndex = question.choices.indexOf(choice)
                quizDao.insertChoice(
                    with(dbLocalMapper) {
                        choice.fromLocalModel(
                            questionIndex,
                            choiceIndex
                        )
                    }
                )
            }
        }
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override fun getQuiz(): Flow<Quiz> =
        quizDao.getQuiz().map { with(dbLocalMapper) { it.toLocalModel() } }
}
