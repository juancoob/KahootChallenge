package com.juancoob.domain

data class Question(
    val type: String,
    val image: String,
    val question: String,
    val choices: List<Choice>,
    val hasPoints: Boolean,
    val pointsMultiplier: Int,
    val time: Int
)
