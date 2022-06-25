package com.juancoob.kahootchallenge.data.server

import com.juancoob.kahootchallenge.data.server.models.Quiz
import retrofit2.http.GET

interface RemoteService {

    @GET("fb4054fc-6a71-463e-88cd-243876715bc1")
    suspend fun getQuiz(): Quiz

}
