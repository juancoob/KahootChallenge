package com.juancoob.kahootchallenge.data.server.models


import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val type: String,
    val image: String,
    val question: String,
    val choices: List<Choice>,
    val pointsMultiplier: Int,
    val time: Long
)
