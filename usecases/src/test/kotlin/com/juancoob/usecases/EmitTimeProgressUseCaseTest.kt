package com.juancoob.usecases

import com.juancoob.data.TimerRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class EmitTimeProgressUseCaseTest {

    @RelaxedMockK
    lateinit var timerRepository: TimerRepository

    private lateinit var emitTimeProgressUseCase: EmitTimeProgressUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        emitTimeProgressUseCase = EmitTimeProgressUseCase(timerRepository)
    }

    @Test
    fun `When the total time sets, the time progress is updated`() {
        emitTimeProgressUseCase.invoke(1000)
        verify { timerRepository.emitTimeProgress(1000) }
    }
}
