package com.juancoob.kahootchallenge

import app.cash.turbine.test
import com.juancoob.data.QuizRepository
import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.database.LocalDataSourceImpl
import com.juancoob.kahootchallenge.data.server.RemoteDataSourceImpl
import com.juancoob.kahootchallenge.fakes.FakeQuizDao
import com.juancoob.kahootchallenge.fakes.FakeRemoteService
import com.juancoob.kahootchallenge.testRules.CoroutineTestRule
import com.juancoob.kahootchallenge.ui.MainViewModel
import com.juancoob.testshared.mockedQuestion
import com.juancoob.testshared.mockedQuiz
import com.juancoob.usecases.GetQuizUseCase
import com.juancoob.usecases.RequestQuizUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainIntegrationTest {

    @get:Rule
    val coroutinesTestRule = CoroutineTestRule()

    private fun viewModelBuilder(localData: Quiz?): MainViewModel {
        val localDataSource = LocalDataSourceImpl(FakeQuizDao(localData))
        val remoteDataSource = RemoteDataSourceImpl(FakeRemoteService(mockedQuiz))
        val quizRepository = QuizRepository(remoteDataSource, localDataSource)
        val requestQuizUseCase = RequestQuizUseCase(quizRepository)
        val getQuizUseCase = GetQuizUseCase(quizRepository)
        return MainViewModel(requestQuizUseCase, getQuizUseCase)
    }

    @Test
    fun `data is loaded from server when local data source is empty`() = runTest {

        val mainViewModel = viewModelBuilder(localData = null)

        mainViewModel.state.test {
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
    fun `data is loaded from DB when local data source is not empty`() = runTest {

        val mockedQuiz = mockedQuiz.copy(
            questions = listOf(
                mockedQuestion,
                mockedQuestion.copy(question = "Do you want to be vegan?")
            )
        )

        val mainViewModel = viewModelBuilder(localData = mockedQuiz)

        mainViewModel.state.test {
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

}
