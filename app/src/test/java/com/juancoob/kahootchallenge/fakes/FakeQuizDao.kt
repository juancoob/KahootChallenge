package com.juancoob.kahootchallenge.fakes

import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.database.QuizDao
import com.juancoob.kahootchallenge.data.database.fromLocalModel
import com.juancoob.kahootchallenge.data.database.models.QuestionWithChoices
import com.juancoob.kahootchallenge.data.database.models.QuizWithQuestions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.juancoob.kahootchallenge.data.database.models.Choice as DbChoice
import com.juancoob.kahootchallenge.data.database.models.Question as DbQuestion
import com.juancoob.kahootchallenge.data.database.models.Quiz as DbQuiz

class FakeQuizDao(quiz: Quiz?) : QuizDao {

    private lateinit var inMemoryQuiz: DbQuiz
    private var inMemoryQuestions: MutableList<DbQuestion> = mutableListOf()
    private var inMemoryChoices: MutableList<DbChoice> = mutableListOf()

    init {
        if (quiz != null) {
            inMemoryQuiz = quiz.fromLocalModel()

            for (question in quiz.questions) {
                val questionIndex = quiz.questions.indexOf(question)
                inMemoryQuestions.add(question.fromLocalModel(quiz.uuid, questionIndex))

                for (choice in question.choices) {
                    val choiceIndex = question.choices.indexOf(choice)
                    inMemoryChoices.add(choice.fromLocalModel(questionIndex, choiceIndex))
                }
            }
        }
    }

    override suspend fun getQuizCount(): Int = if (::inMemoryQuiz.isInitialized) 1 else 0

    override suspend fun insertQuiz(quiz: DbQuiz) {
        inMemoryQuiz = quiz
    }

    override suspend fun insertQuestion(question: DbQuestion) {
        inMemoryQuestions.add(question)
    }

    override suspend fun insertChoice(choice: DbChoice) {
        inMemoryChoices.add(choice)
    }

    override fun getQuiz(): Flow<QuizWithQuestions> {
        val questionWithChoices = mutableListOf<QuestionWithChoices>()
        inMemoryQuestions.forEach { question ->
            val element = QuestionWithChoices(question, inMemoryChoices.filter { it.questionId == question.id })
            questionWithChoices.add(element)
        }
        val quizWithChoices = QuizWithQuestions(inMemoryQuiz, questionWithChoices)
        return flowOf(quizWithChoices)
    }
}
