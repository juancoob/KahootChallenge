package com.juancoob.kahootchallenge.data.server

import com.juancoob.domain.Quiz
import com.juancoob.testshared.mockedQuiz
import org.junit.Assert.*
import org.junit.Test
import com.juancoob.kahootchallenge.data.server.models.Quiz as ServerQuiz

class RemoteLocalMapperTest {

    private val remoteLocalMapper: RemoteLocalMapper = RemoteLocalMapper()

    @Test
    fun `Map from local quiz to server quiz and backwards`() {
        val serverQuiz: ServerQuiz = with(remoteLocalMapper) { mockedQuiz.fromLocalModel() }
        val actualLocalQuiz: Quiz = with(remoteLocalMapper) { serverQuiz.toLocalModel() }
        assertEquals(mockedQuiz, actualLocalQuiz)
    }
}
