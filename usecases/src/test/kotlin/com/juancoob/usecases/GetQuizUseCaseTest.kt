package com.juancoob.usecases

import com.juancoob.data.QuizRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetQuizUseCaseTest {

    @RelaxedMockK
    lateinit var quizRepository: QuizRepository

    private lateinit var getQuizUseCase: GetQuizUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getQuizUseCase = GetQuizUseCase(quizRepository)
    }

    @Test
    fun `When the quiz is stored, the app retrieves it`() {
        getQuizUseCase.invoke()
        verify { quizRepository.getQuiz() }
    }
}
