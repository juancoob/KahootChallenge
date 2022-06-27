package com.juancoob.usecases

import com.juancoob.data.QuizRepository
import com.juancoob.domain.Quiz
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuizUseCase @Inject constructor(private val quizRepository: QuizRepository) {
    operator fun invoke(): Flow<Quiz> =
        quizRepository.getQuiz()
}
