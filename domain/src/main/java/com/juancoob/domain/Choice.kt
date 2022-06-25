package com.juancoob.domain

data class Choice(
    val text: String,
    val isCorrect: Boolean,
    val isSelected: Boolean,
    val showAnswer: Boolean
)
