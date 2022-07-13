package com.juancoob.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TimerRepository @Inject constructor() {

    fun emitTimeProgress(timeInMillis: Long) = flow {
        var counter = 1
        var millisUntilFinished: Long = timeInMillis
        while (millisUntilFinished >= 0) {
            delay(ONE_SECOND_IN_MILLIS)
            millisUntilFinished = timeInMillis - ONE_SECOND_IN_MILLIS * counter
            emit((millisUntilFinished * ONE_HUNDRED_PERCENT / timeInMillis).toInt())
            counter++
        }
        emit(0)
    }

    companion object {
        private const val ONE_SECOND_IN_MILLIS = 1000L
        private const val ONE_HUNDRED_PERCENT = 100
    }
}
