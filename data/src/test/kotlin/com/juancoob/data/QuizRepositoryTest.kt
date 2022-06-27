package com.juancoob.data

import arrow.core.left
import arrow.core.right
import com.juancoob.data.datasource.LocalDataSource
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.testshared.mockedQuiz
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class QuizRepositoryTest {

    @RelaxedMockK
    lateinit var localDataSource: LocalDataSource

    @RelaxedMockK
    lateinit var remoteDataSource: RemoteDataSource

    private lateinit var quizRepository: QuizRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        quizRepository = QuizRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun `When the app request a quiz, the remote data source does it and the local data source inserts it`() =
        runTest {
            val errorExpected = null
            coEvery { localDataSource.isEmpty() }.returns(true)
            coEvery { remoteDataSource.requestQuiz() }.returns(mockedQuiz.right())
            coEvery { localDataSource.insertQuizData(mockedQuiz) }.returns(null)

            val errorRetrieved = quizRepository.requestQuiz()

            assertTrue(errorRetrieved == errorExpected)
        }

    @Test
    fun `When the local data source inserts data and an error occurs, it retrieves an ErrorRetrieved`() =
        runTest {
            val errorExpected = ErrorRetrieved.Unknown("Error")
            coEvery { localDataSource.isEmpty() }.returns(true)
            coEvery { remoteDataSource.requestQuiz() }.returns(mockedQuiz.right())
            coEvery { localDataSource.insertQuizData(mockedQuiz) }.returns(errorExpected)

            val errorRetrieved = quizRepository.requestQuiz()

            assertTrue(errorRetrieved == errorExpected)
        }

    @Test
    fun `When the remote data source request data and an error occurs, it retrieves an ErrorRetrieved`() =
        runTest {
            val errorExpected = ErrorRetrieved.Unknown("Error")
            coEvery { localDataSource.isEmpty() }.returns(true)
            coEvery { remoteDataSource.requestQuiz() }.returns(errorExpected.left())

            val errorRetrieved = quizRepository.requestQuiz()

            assertTrue(errorRetrieved == errorExpected)
        }

    @Test
    fun `When the local data source is not empty, the app won't retrieve data from the server`() =
        runTest {
            val errorExpected = null
            coEvery { localDataSource.isEmpty() }.returns(false)

            val errorRetrieved = quizRepository.requestQuiz()

            assertTrue(errorRetrieved == errorExpected)
        }

    @Test
    fun `When the app retrieves a quiz, it calls to the local data source`() {
        quizRepository.getQuiz()
        verify { localDataSource.getQuiz() }
    }
}
