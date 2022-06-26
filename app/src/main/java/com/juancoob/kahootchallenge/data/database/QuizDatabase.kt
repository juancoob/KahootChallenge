package com.juancoob.kahootchallenge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juancoob.kahootchallenge.data.database.models.Choice
import com.juancoob.kahootchallenge.data.database.models.Question
import com.juancoob.kahootchallenge.data.database.models.Quiz

@Database(entities = [Quiz::class, Question::class, Choice::class], version = 1, exportSchema = false)
abstract class QuizDatabase: RoomDatabase() {
    abstract fun quizDao(): QuizDao
}
