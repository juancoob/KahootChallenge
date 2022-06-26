package com.juancoob.domain

data class Quiz(
    val uuid: String,
    val quizType: String,
    val title: String,
    val description: String,
    val questions: List<Question>
)
