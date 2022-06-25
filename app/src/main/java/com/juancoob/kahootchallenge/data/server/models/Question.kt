package com.juancoob.kahootchallenge.data.server.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val type: String,
    val image: String,
    val question: String,
    val choices: List<Choice>,
    @SerialName("points")
    val hasPoints: Boolean,
    val pointsMultiplier: Int,
    val time: Int
)
