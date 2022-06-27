package com.juancoob.kahootchallenge.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.juancoob.kahootchallenge.data.database.models.Choice
import com.juancoob.kahootchallenge.data.database.models.Question
import com.juancoob.kahootchallenge.data.database.models.Quiz
import com.juancoob.kahootchallenge.data.database.models.QuizWithQuestions
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Query("SELECT COUNT(uuid) FROM Quiz")
    suspend fun getQuizCount(): Int

    @Insert(onConflict = REPLACE)
    suspend fun insertQuiz(quiz: Quiz)

    @Insert(onConflict = REPLACE)
    suspend fun insertQuestion(question: Question)

    @Insert(onConflict = REPLACE)
    suspend fun insertChoice(choice: Choice)

    @Transaction
    @Query("SELECT * FROM Quiz")
    fun getQuiz(): Flow<QuizWithQuestions>
}
