package com.juancoob.usecases

import com.juancoob.data.TimerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmitTimeProgressUseCase @Inject constructor(private val timerRepository: TimerRepository) {
    operator fun invoke(timeInMillis: Long): Flow<Int> = timerRepository.emitTimeProgress(timeInMillis)
}
