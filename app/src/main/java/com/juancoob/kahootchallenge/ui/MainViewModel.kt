package com.juancoob.kahootchallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juancoob.domain.Choice
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Question
import com.juancoob.domain.Quiz
import com.juancoob.kahootchallenge.data.toErrorRetrieved
import com.juancoob.usecases.GetQuizUseCase
import com.juancoob.usecases.RequestQuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

@HiltViewModel
class MainViewModel @Inject constructor(
    private val requestQuizUseCase: RequestQuizUseCase,
    private val getQuizUseCase: GetQuizUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    private var quiz: Quiz? = null
    private var questionIndex: Int = 0
    private var currentQuestion: Question? = null
    private var jobToUpdateTimeProgress: Job? = null
    private var countDownTimerToUpdateTimeProgress: Timer? = null
    private var jobToGoToNextQuestion: Job? = null

    init {
        requestData()
    }

    fun requestData() {
        _state.value = UiState(loading = true)
        viewModelScope.launch {
            val errorRetrieved = requestQuizUseCase()
            if (errorRetrieved == null) {
                getQuizUseCase()
                    .catch { cause ->
                        _state.update {
                            _state.value.copy(
                                loading = false,
                                errorRetrieved = cause.toErrorRetrieved(),
                                onRetry = ::requestData
                            )
                        }
                    }
                    .collect { quizCollected ->
                        quiz = quizCollected
                        questionIndex = 0
                        _state.update {
                            _state.value.copy(
                                loading = false,
                                numberOfQuestions = quiz!!.questions.size,
                                onRetrieveQuestion = ::retrieveQuestion
                            )
                        }
                    }
            } else {
                _state.value = _state.value.copy(
                    loading = false,
                    errorRetrieved = errorRetrieved,
                    onRetry = ::requestData
                )
            }
        }
    }

    fun retrieveQuestion() {
        stopJobToGoToNextQuestion()

        if (questionIndex < quiz!!.questions.size) {
            currentQuestion = quiz!!.questions[questionIndex]
            _state.update { uiState ->
                _state.value.copy(
                    isCorrectChoice = null,
                    question = currentQuestion,
                    questionNumber = questionIndex + 1,
                    choiceUiStateList = currentQuestion!!.choices.map { it.toChoiceUiState() },
                    timeProgressPercentage = ONE_HUNDRED_PERCENT,
                    points = if (questionIndex == 0) 0 else uiState.points,
                )
            }
            questionIndex += 1
            startCountDownTimerToUpdateTimeProgress(currentQuestion!!.time)
        } else {
            _state.update {
                _state.value.copy(
                    isCorrectChoice = null,
                    question = null,
                    choiceUiStateList = null
                )
            }
            questionIndex = 0
        }
    }

    private fun stopJobToGoToNextQuestion() {
        jobToGoToNextQuestion?.cancel()
    }

    private fun startCountDownTimerToUpdateTimeProgress(timeInMillis: Long) {
        jobToUpdateTimeProgress = viewModelScope.launch {
            var counter = 1
            var millisUntilFinished: Long = timeInMillis
            countDownTimerToUpdateTimeProgress = fixedRateTimer(period = ONE_SECOND_IN_MILLIS) {
                _state.update {
                    _state.value.copy(
                        timeProgressPercentage = (millisUntilFinished * ONE_HUNDRED_PERCENT / timeInMillis).toInt()
                    )
                }
                millisUntilFinished = timeInMillis - ONE_SECOND_IN_MILLIS * counter
                counter++
            }
            delay(timeInMillis)
            stopTimerToUpdateTimeProgress()
            _state.update {
                _state.value.copy(
                    isCorrectChoice = false,
                    choiceUiStateList = it.choiceUiStateList!!.map { choiceUiState ->
                        choiceUiState.copy(
                            choice = choiceUiState.choice.copy(
                                showAnswer = true
                            )
                        )
                    },
                    timeProgressPercentage = 0
                )
            }
            startCountDownTimerToGoToNextQuestion()
        }
    }

    private fun stopTimerToUpdateTimeProgress() {
        countDownTimerToUpdateTimeProgress?.cancel()
    }

    private fun startCountDownTimerToGoToNextQuestion() {
        jobToGoToNextQuestion = viewModelScope.launch {
            delay(DEFAULT_TIME_IN_MILLIS_TO_GO_TO_NEXT_QUESTION)
            retrieveQuestion()
        }
    }

    fun Choice.toChoiceUiState() = ChoiceUiState(
        choice = this,
        onClickChoice = {
            _state.update {
                _state.value.copy(
                    isCorrectChoice = isCorrect,
                    choiceUiStateList = it.choiceUiStateList!!.map { choiceUiState ->
                        choiceUiState.copy(
                            choice = choiceUiState.choice.copy(
                                isSelected = choiceUiState.choice.text == text,
                                showAnswer = true
                            )
                        )
                    },
                    points = if (isCorrect) {
                        it.points?.plus(it.question!!.pointsMultiplier)
                    } else {
                        it.points
                    }
                )
            }
            stopTimerToUpdateTimeProgress()
            stopJobToUpdateTimeProgress()
            startCountDownTimerToGoToNextQuestion()
        }
    )

    private fun stopJobToUpdateTimeProgress() {
        jobToUpdateTimeProgress?.cancel()
    }

    data class UiState(
        val loading: Boolean = false,
        val onRetrieveQuestion: (() -> Unit)? = null,
        val isCorrectChoice: Boolean? = null,
        val question: Question? = null,
        val questionNumber: Int? = null,
        val numberOfQuestions: Int? = null,
        val choiceUiStateList: List<ChoiceUiState>? = null,
        val timeProgressPercentage: Int? = null,
        val points: Int? = null,
        val errorRetrieved: ErrorRetrieved? = null,
        val onRetry: (() -> Unit)? = null
    )

    data class ChoiceUiState(
        val choice: Choice,
        val onClickChoice: () -> Unit
    )

    companion object {
        const val ONE_SECOND_IN_MILLIS = 1000L
        const val DEFAULT_TIME_IN_MILLIS_TO_GO_TO_NEXT_QUESTION = 10000L
        const val ONE_HUNDRED_PERCENT = 100
    }
}
