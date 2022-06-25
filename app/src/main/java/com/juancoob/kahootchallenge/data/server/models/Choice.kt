package com.juancoob.kahootchallenge.data.server.models


import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    val answer: String,
    val correct: Boolean,
)
