package com.juancoob.kahootchallenge.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(
    @PrimaryKey val id: Int,
    val quizId: String,
    val type: String,
    val image: String,
    val question: String,
    val pointsMultiplier: Int,
    val time: Long
)
