package com.juancoob.kahootchallenge.fakes

import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.server.RemoteLocalMapper
import com.juancoob.kahootchallenge.data.server.RemoteService
import com.juancoob.kahootchallenge.data.server.models.Quiz as ServerQuiz

class FakeRemoteService(private val quiz: Quiz) : RemoteService {

    private val remoteLocalMapper: RemoteLocalMapper = RemoteLocalMapper()

    override suspend fun getQuiz(): ServerQuiz = with(remoteLocalMapper) { quiz.fromLocalModel() }
}
