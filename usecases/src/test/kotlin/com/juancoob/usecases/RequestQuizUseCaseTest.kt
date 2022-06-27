package com.juancoob.usecases

import com.juancoob.data.QuizRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RequestQuizUseCaseTest {

    @RelaxedMockK
    lateinit var quizRepository: QuizRepository

    private lateinit var requestQuizUseCase: RequestQuizUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        requestQuizUseCase = RequestQuizUseCase(quizRepository)
    }

    @Test
    fun `When the app starts, the quiz is requested`() = runTest {
        requestQuizUseCase.invoke()
        coVerify { quizRepository.requestQuiz() }
    }
}
