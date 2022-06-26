package com.juancoob.kahootchallenge.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quiz(
    @PrimaryKey val uuid: String,
    val title: String,
    val description: String,
    val quizType: String
)
