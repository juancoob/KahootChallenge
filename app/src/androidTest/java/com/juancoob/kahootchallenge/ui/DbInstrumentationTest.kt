package com.juancoob.kahootchallenge.ui

import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.database.DbLocalMapper
import com.juancoob.kahootchallenge.data.database.QuizDao
import com.juancoob.testshared.mockedQuiz
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class DbInstrumentationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var quizDao: QuizDao

    @Inject
    lateinit var dbLocalMapper: DbLocalMapper

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun when_the_database_is_empty_the_quiz_count_is_0() = runTest {
        val actualCount = quizDao.getQuizCount()

        assertEquals(0, actualCount)
    }

    @Test
    fun when_the_database_has_a_quiz_some_questions_and_choices_the_database_retrieves_a_local_quiz() =
        runTest {
            insertQuizData()

            val actualQuiz: Quiz =
                quizDao.getQuiz().map { with(dbLocalMapper) { it.toLocalModel() } }.first()

            assertEquals(mockedQuiz, actualQuiz)
        }

    @Test
    fun when_the_database_is_not_empty_the_quiz_count_is_1() = runTest {
        when_the_database_has_a_quiz_some_questions_and_choices_the_database_retrieves_a_local_quiz()

        val actualCount = quizDao.getQuizCount()

        assertEquals(1, actualCount)
    }

    private suspend fun insertQuizData() {
        var questionIndex: Int
        var choiceIndex: Int

        quizDao.insertQuiz(with(dbLocalMapper) { mockedQuiz.fromLocalModel() })

        for (question in mockedQuiz.questions) {
            questionIndex = mockedQuiz.questions.indexOf(question)
            quizDao.insertQuestion(
                with(dbLocalMapper) {
                    question.fromLocalModel(
                        mockedQuiz.uuid,
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
    }
}
