package com.juancoob.usecases

import com.juancoob.data.QuizRepository
import com.juancoob.domain.ErrorRetrieved
import javax.inject.Inject

class RequestQuizUseCase @Inject constructor(private val quizRepository: QuizRepository) {
    suspend operator fun invoke(): ErrorRetrieved? =
        quizRepository.requestQuiz()
}
