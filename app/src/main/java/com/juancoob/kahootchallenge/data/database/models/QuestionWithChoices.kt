package com.juancoob.kahootchallenge.data.database.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class QuestionWithChoices(
    @Embedded val question: Question,
    @Relation(
        entity = Choice::class,
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val choices: List<Choice>
)
