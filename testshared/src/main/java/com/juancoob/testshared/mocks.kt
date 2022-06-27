package com.juancoob.testshared

import com.juancoob.domain.Choice
import com.juancoob.domain.Question
import com.juancoob.domain.Quiz

val mockedQuiz: Quiz
    get() = Quiz(
        uuid = "fb4054fc-6a71-463e-88cd-243876715bc1",
        quizType = "quiz",
        title = "Seven Wonders of the Ancient World",
        description = "A #geography #quiz about the #Seven #Wonders of the #Ancient #World." +
                "See how much you know about these ancient buildings and monuments!\n#trivia #history #studio",
        questions = listOf(mockedQuestion)
    )

val mockedQuestion: Question
    get() = Question(
        type = "quiz",
        image = "https://media.kahoot.it/8d195530-548a-423e-8604-47f0cabd96e6_opt",
        question = "<b>True or false: </b>The list of seven wonders is based on ancient Greek guidebooks for tourists.",
        choices = listOf(mockedChoice),
        hasPoints = true,
        pointsMultiplier = 1,
        time = 20000
    )

val mockedChoice = Choice(
    text = "TRUE",
    isCorrect = true,
    isSelected = false,
    showAnswer = false
)
