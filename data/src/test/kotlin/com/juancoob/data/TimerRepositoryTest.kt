package com.juancoob.data

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class TimerRepositoryTest {

    private val timerRepository = TimerRepository()

    @Test
    fun `When the total time sets, the time progress gets updated`() = runTest {
        val totalTimeInMillis = 4000L
        val firstExpectedPercentage = 75
        val secondExpectedPercentage = 50
        val thirdExpectedPercentage = 25
        val fourthExpectedPercentage = 0
        timerRepository.emitTimeProgress(totalTimeInMillis).test {
            assertEquals(firstExpectedPercentage, awaitItem())
            assertEquals(secondExpectedPercentage, awaitItem())
            assertEquals(thirdExpectedPercentage, awaitItem())
            assertEquals(fourthExpectedPercentage, awaitItem())
        }
    }

}
