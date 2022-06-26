package com.juancoob.kahootchallenge.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Choice(
    @PrimaryKey val id: Int,
    val questionId: Int,
    val answer: String,
    val correct: Boolean,
)
