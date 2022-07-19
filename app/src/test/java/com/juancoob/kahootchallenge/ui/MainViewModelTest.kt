package com.juancoob.kahootchallenge.ui

import app.cash.turbine.test
import com.juancoob.domain.Choice
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.kahootchallenge.testRules.CoroutineTestRule
import com.juancoob.testshared.mockedChoice
import com.juancoob.testshared.mockedQuestion
import com.juancoob.testshared.mockedQuiz
import com.juancoob.usecases.EmitTimeProgressUseCase
import com.juancoob.usecases.GetQuizUseCase
import com.juancoob.usecases.RequestQuizUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var requestQuizUseCase: RequestQuizUseCase

    @MockK
    private lateinit var getQuizUseCase: GetQuizUseCase

    @MockK
    private lateinit var emitTimeProgressUseCase: EmitTimeProgressUseCase

    @MockK
    private lateinit var choiceUiStateMapper: ChoiceUiStateMapper

    @MockK
    private lateinit var lambda: () -> Unit

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mainViewModel = MainViewModel(
            requestQuizUseCase,
            getQuizUseCase,
            emitTimeProgressUseCase,
            choiceUiStateMapper
        )
    }

    @Test
    fun `When the ViewModel request a quiz, the app retrieves it`() = runTest {
        mainViewModel.state.test {
            coEvery { requestQuizUseCase() } returns null
            coEvery { getQuizUseCase() } returns flowOf(mockedQuiz)
            assertEquals(MainViewModel.UiState(loading = true), awaitItem())
            assertEquals(
                MainViewModel.UiState(
                    loading = false,
                    numberOfQuestions = mockedQuiz.questions.size,
                    onRetrieveQuestion = mainViewModel::retrieveQuestion
                ), awaitItem()
            )
        }
    }

    @Test
    fun `When the ViewModel request a quiz and an error occurs in the DB, the app shows an error`() =
        runTest {
            mainViewModel.state.test {
                coEvery { requestQuizUseCase() } returns null
                coEvery { getQuizUseCase() } returns flow { throw IOException() }
                assertEquals(MainViewModel.UiState(loading = true), awaitItem())
                assertEquals(
                    MainViewModel.UiState(
                        loading = false,
                        errorRetrieved = ErrorRetrieved.Connectivity,
                        onRetry = mainViewModel::requestData
                    ), awaitItem()
                )
            }
        }

    @Test
    fun `When the ViewModel can not retrieve a quiz due to a server error, the app notifies it`() =
        runTest {
            mainViewModel.state.test {
                val expectedServerError = ErrorRetrieved.Server(401)
                coEvery { requestQuizUseCase() } returns expectedServerError
                assertEquals(MainViewModel.UiState(loading = true), awaitItem())
                assertEquals(
                    MainViewModel.UiState(
                        loading = false,
                        errorRetrieved = expectedServerError,
                        onRetry = mainViewModel::requestData
                    ), awaitItem()
                )
            }
        }

    @Test
    fun `When the ViewModel can not retrieve a quiz due to an unknown error, the app notifies it`() =
        runTest {
            mainViewModel.state.test {
                val expectedServerError = ErrorRetrieved.Unknown("Unknown error")
                coEvery { requestQuizUseCase() } returns expectedServerError
                assertEquals(MainViewModel.UiState(loading = true), awaitItem())
                assertEquals(
                    MainViewModel.UiState(
                        loading = false,
                        errorRetrieved = expectedServerError,
                        onRetry = mainViewModel::requestData
                    ), awaitItem()
                )
            }
        }

    @Test
    fun `When the app calls onRetrieveQuestion, it loads the question data and progress bar updates`() =
        runTest {
            `When the ViewModel request a quiz, the app retrieves it`()
            mainViewModel.state.test {
                with(choiceUiStateMapper) {
                    every { any<Choice>().toChoiceUiState(any()) } returns MainViewModel.ChoiceUiState(
                        choice = mockedChoice,
                        onClickChoice = lambda
                    )
                }
                val expectedQuestionNumber = 1
                val expectedPoints = 0
                val expectedInitialTimeProgressPercentage = 100
                val expectedNextTimeProgressPercentage = 75
                every { emitTimeProgressUseCase(any()) } returns flowOf(
                    expectedNextTimeProgressPercentage
                )

                mainViewModel.retrieveQuestion()

                assertEquals(
                    MainViewModel.UiState(
                        loading = false,
                        numberOfQuestions = mockedQuiz.questions.size,
                        onRetrieveQuestion = mainViewModel::retrieveQuestion
                    ), awaitItem()
                )
                assertEquals(
                    MainViewModel.UiState(
                        isCorrectChoice = null,
                        question = mockedQuestion,
                        questionNumber = expectedQuestionNumber,
                        numberOfQuestions = mockedQuiz.questions.size,
                        onRetrieveQuestion = mainViewModel::retrieveQuestion,
                        choiceUiStateList = mockedQuestion.choices.map {
                            with(choiceUiStateMapper) {
                                it.toChoiceUiState(lambda)
                            }
                        },
                        timeProgressPercentage = expectedInitialTimeProgressPercentage,
                        points = expectedPoints
                    ), awaitItem()
                )
                assertEquals(
                    MainViewModel.UiState(
                        isCorrectChoice = null,
                        question = mockedQuestion,
                        questionNumber = expectedQuestionNumber,
                        numberOfQuestions = mockedQuiz.questions.size,
                        onRetrieveQuestion = mainViewModel::retrieveQuestion,
                        choiceUiStateList = mockedQuestion.choices.map {
                            with(choiceUiStateMapper) {
                                it.toChoiceUiState(lambda)
                            }
                        },
                        timeProgressPercentage = expectedNextTimeProgressPercentage,
                        points = expectedPoints
                    ), awaitItem()
                )
            }
        }

    @Test
    fun `When the app calls onRetrieveQuestion and the user didn't click, it shows times up state`() =
        runTest {
            `When the ViewModel request a quiz, the app retrieves it`()
            mainViewModel.state.test {
                with(choiceUiStateMapper) {
                    every { any<Choice>().toChoiceUiState(any()) } returns MainViewModel.ChoiceUiState(
                        choice = mockedChoice.copy(showAnswer = true),
                        onClickChoice = lambda
                    )
                }
                val expectedQuestionNumber = 1
                val expectedPoints = 0
                val expectedInitialTimeProgressPercentage = 100
                val expectedNextTimeProgressPercentage = 0
                every { emitTimeProgressUseCase(any()) } returns flowOf(
                    expectedNextTimeProgressPercentage
                )

                mainViewModel.retrieveQuestion()

                assertEquals(
                    MainViewModel.UiState(
                        loading = false,
                        numberOfQuestions = mockedQuiz.questions.size,
                        onRetrieveQuestion = mainViewModel::retrieveQuestion
                    ), awaitItem()
                )
                assertEquals(
                    MainViewModel.UiState(
                        isCorrectChoice = null,
                        question = mockedQuestion,
                        questionNumber = expectedQuestionNumber,
                        numberOfQuestions = mockedQuiz.questions.size,
                        onRetrieveQuestion = mainViewModel::retrieveQuestion,
                        choiceUiStateList = mockedQuestion.choices.map {
                            with(choiceUiStateMapper) {
                                it.toChoiceUiState(lambda)
                            }
                        },
                        timeProgressPercentage = expectedInitialTimeProgressPercentage,
                        points = expectedPoints
                    ), awaitItem()
                )
                assertEquals(
                    MainViewModel.UiState(
                        isCorrectChoice = false,
                        question = mockedQuestion,
                        questionNumber = expectedQuestionNumber,
                        numberOfQuestions = mockedQuiz.questions.size,
                        onRetrieveQuestion = mainViewModel::retrieveQuestion,
                        choiceUiStateList = mockedQuestion.choices.map {
                            with(choiceUiStateMapper) {
                                it.copy(showAnswer = true).toChoiceUiState(lambda)
                            }
                        },
                        timeProgressPercentage = expectedNextTimeProgressPercentage,
                        points = expectedPoints
                    ), awaitItem()
                )
            }
        }

    @Test
    fun `When all questions have been sent, the app shows the final ui state`() =
        runTest {
            `When the app calls onRetrieveQuestion, it loads the question data and progress bar updates`()
            val expectedQuestionNumber = 1
            val expectedPoints = 0
            val expectedPreviousTimeProgressPercentage = 75

            mainViewModel.state.test {

                mainViewModel.retrieveQuestion()

                assertEquals(
                    MainViewModel.UiState(
                        isCorrectChoice = null,
                        question = mockedQuestion,
                        questionNumber = expectedQuestionNumber,
                        numberOfQuestions = mockedQuiz.questions.size,
                        onRetrieveQuestion = mainViewModel::retrieveQuestion,
                        choiceUiStateList = mockedQuestion.choices.map {
                            with(choiceUiStateMapper) {
                                it.toChoiceUiState(lambda)
                            }
                        },
                        timeProgressPercentage = expectedPreviousTimeProgressPercentage,
                        points = expectedPoints
                    ), awaitItem()
                )

                assertEquals(
                    MainViewModel.UiState(
                        isCorrectChoice = null,
                        question = null,
                        questionNumber = expectedQuestionNumber,
                        numberOfQuestions = mockedQuiz.questions.size,
                        onRetrieveQuestion = mainViewModel::retrieveQuestion,
                        choiceUiStateList = null,
                        timeProgressPercentage = expectedPreviousTimeProgressPercentage,
                        points = expectedPoints
                    ), awaitItem()
                )
            }
        }

}
