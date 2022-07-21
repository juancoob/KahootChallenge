package com.juancoob.kahootchallenge.data.server.dispatcher

import android.util.Log
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader


class RequestDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse = when {
        request.path?.contains("fb4054fc-6a71-463e-88cd-243876715bc1") == true ->
            MockResponse().setResponseCode(200).setBody(readJsonFile())
        else -> MockResponse().setResponseCode(400)
    }

    private fun readJsonFile() = try {
        InputStreamReader(this.javaClass.classLoader?.getResourceAsStream("kahootQuiz.json")).use { inputStreamReader ->
            inputStreamReader.readText()
        }
    } catch (ioException: IOException) {
        Log.e(RequestDispatcher::class.qualifiedName, ioException.message ?: "")
        ""
    }
}


class RequestDispatcherWithEmptyResult : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse = when {
        request.path?.contains("fb4054fc-6a71-463e-88cd-243876715bc1") == true ->
            MockResponse().setResponseCode(200).setBody(JSONObject().toString())
        else -> MockResponse().setResponseCode(400)
    }
}

class RequestDispatcherWithError : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse =
        MockResponse().setResponseCode(400)
}
