package com.juancoob.kahootchallenge

import app.cash.turbine.test
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.kahootchallenge.testRules.CoroutineTestRule
import com.juancoob.kahootchallenge.ui.ChoiceUiStateMapper
import com.juancoob.kahootchallenge.ui.MainViewModel
import com.juancoob.testshared.mockedChoice
import com.juancoob.testshared.mockedQuestion
import com.juancoob.testshared.mockedQuiz
import com.juancoob.usecases.GetQuizUseCase
import com.juancoob.usecases.RequestQuizUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var requestQuizUseCase: RequestQuizUseCase

    @RelaxedMockK
    lateinit var getQuizUseCase: GetQuizUseCase

    @RelaxedMockK
    lateinit var choiceUiStateMapper: ChoiceUiStateMapper

    @RelaxedMockK
    lateinit var lambda: () -> Unit

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mainViewModel = MainViewModel(
            requestQuizUseCase,
            getQuizUseCase,
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
            cancel()
        }
    }

    @Test
    fun `When the ViewModel can not retrieve a quiz, it shows an error`() = runTest {
        mainViewModel.state.test {
            coEvery { requestQuizUseCase() } returns ErrorRetrieved.Connectivity
            assertEquals(MainViewModel.UiState(loading = true), awaitItem())
            assertEquals(
                MainViewModel.UiState(
                    loading = false,
                    errorRetrieved = ErrorRetrieved.Connectivity,
                    onRetry = mainViewModel::requestData
                ), awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun `When the app calls onRetrieveQuestion, it loads the question data`() = runTest {
        `When the ViewModel request a quiz, the app retrieves it`()
        mainViewModel.state.test {
            with(choiceUiStateMapper) {
                every { mockedChoice.toChoiceUiState(any()) } returns MainViewModel.ChoiceUiState(
                    mockedChoice,
                    lambda
                )
            }

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
                    questionNumber = 1,
                    numberOfQuestions = mockedQuiz.questions.size,
                    onRetrieveQuestion = mainViewModel::retrieveQuestion,
                    choiceUiStateList = mockedQuestion.choices.map {
                        with(choiceUiStateMapper) {
                            it.toChoiceUiState(lambda)
                        }
                    },
                    timeProgressPercentage = 100,
                    points = 0
                ), awaitItem()
            )
            cancel()
        }
    }
}
