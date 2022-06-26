package com.juancoob.kahootchallenge.data.database.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class QuizWithQuestions(
    @Embedded val quiz: Quiz,
    @Relation(
        entity = Question::class,
        parentColumn = "uuid",
        entityColumn = "quizId"
    )
    val questions: List<QuestionWithChoices>
)
