package com.juancoob.kahootchallenge.data.server.models


import kotlinx.serialization.Serializable

@Serializable
data class Quiz(
    val uuid: String,
    val title: String,
    val description: String,
    val questions: List<Question>,
    val quizType: String
)
