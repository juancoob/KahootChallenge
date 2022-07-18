package com.juancoob.kahootchallenge.data.database

import com.juancoob.kahootchallenge.data.database.models.QuestionWithChoices
import com.juancoob.kahootchallenge.data.database.models.QuizWithQuestions
import com.juancoob.testshared.mockedQuiz
import org.junit.Assert.assertEquals
import org.junit.Test
import com.juancoob.kahootchallenge.data.database.models.Choice as DbChoice
import com.juancoob.kahootchallenge.data.database.models.Question as DbQuestion
import com.juancoob.kahootchallenge.data.database.models.Quiz as DbQuiz

class DbLocalMapperTest {

    private val dbLocalMapper: DbLocalMapper = DbLocalMapper()
    private var questionIndex: Int = -1
    private val dbChoiceList: MutableList<DbChoice> = mutableListOf()
    private lateinit var currentDbQuestion: DbQuestion
    private var choiceIndex: Int = -1
    private val questionWithChoices: MutableList<QuestionWithChoices> = mutableListOf()
    private lateinit var quizWithQuestions: QuizWithQuestions

    @Test
    fun `Map from local quiz to DB quiz and backwards`() {
        val dbQuiz: DbQuiz = with(dbLocalMapper) { mockedQuiz.fromLocalModel() }

        for (question in mockedQuiz.questions) {
            questionIndex = mockedQuiz.questions.indexOf(question)
            currentDbQuestion = with(dbLocalMapper) {
                question.fromLocalModel(
                    mockedQuiz.uuid,
                    questionIndex
                )
            }

            for (choice in question.choices) {
                choiceIndex = question.choices.indexOf(choice)
                dbChoiceList.add(
                    with(dbLocalMapper) {
                        choice.fromLocalModel(
                            questionIndex,
                            choiceIndex
                        )
                    }
                )
            }
            questionWithChoices.add(
                QuestionWithChoices(
                    question = currentDbQuestion,
                    choices = dbChoiceList.toList()
                )
            )
            dbChoiceList.clear()
        }
        quizWithQuestions = QuizWithQuestions(
            quiz = dbQuiz,
            questions = questionWithChoices
        )

        val actualResultLocalQuiz = with(dbLocalMapper) { quizWithQuestions.toLocalModel() }

        assertEquals(mockedQuiz, actualResultLocalQuiz)
    }
}
