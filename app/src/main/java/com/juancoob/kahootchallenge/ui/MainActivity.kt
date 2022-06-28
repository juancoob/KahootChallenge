package com.juancoob.kahootchallenge.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Question
import com.juancoob.kahootchallenge.R
import com.juancoob.kahootchallenge.common.loadUrl
import com.juancoob.kahootchallenge.databinding.ActivityMainBinding
import com.juancoob.kahootchallenge.ui.MainViewModel.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initQuizChoices()
        collectUiState()
    }

    private fun initQuizChoices() {
        adapter = MainAdapter()
        binding.choiceList.adapter = adapter
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.state.collect {
                    shouldShowProgressBar(it.loading)
                    shouldShowChoiceTypeBar(it.isCorrectChoice, it.timeProgressPercentage)
                    shouldShowUserMessage(it)
                    shouldPopulateQuestionScreen(it)
                    shouldInitContinueButtonListenerToRetrieveNextQuestion(it.onRetrieveQuestion)
                    shouldShowContinueButton(it.isCorrectChoice)
                    shouldShowErrorRetrieved(it.errorRetrieved, it.onRetry)
                }
            }
        }
    }

    private fun shouldShowProgressBar(showLoading: Boolean) {
        binding.loading.isVisible = showLoading
    }

    private fun shouldShowChoiceTypeBar(correctChoice: Boolean?, timeProgressPercentage: Int?) {
        binding.choiceTypeBar.apply {
            isVisible = correctChoice != null
            if (correctChoice != null && correctChoice == true) {
                setBackgroundColor(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.correct_answer_background
                    )
                )
                text = getString(R.string.correct)
            } else if (correctChoice != null && timeProgressPercentage == 0) {
                setBackgroundColor(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.default_wrong_answer_background
                    )
                )
                text = getString(R.string.times_up)
            } else if (correctChoice != null) {
                setBackgroundColor(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.wrong_answer_background
                    )
                )
                text = getString(R.string.wrong)
            }
        }
    }

    private fun shouldShowUserMessage(uiState: UiState) = binding.run {
        questionNumber.isVisible = uiState.question != null
        questionType.isVisible = uiState.question != null
        questionImage.isVisible = uiState.question != null
        questionText.isVisible = uiState.question != null
        choiceList.isVisible = uiState.question != null
        remainingTime.isVisible = uiState.question != null
        message.apply {
            isVisible =
                uiState.questionNumber == null
                        || uiState.question == null
            if (isVisible) {
                text = if (uiState.points == null) {
                    getString(R.string.welcome_sentence)
                } else {
                    when {
                        uiState.points <= uiState.numberOfQuestions!! / THREE_LEVELS -> {
                            getString(R.string.few_points, uiState.points)
                        }
                        uiState.points > uiState.numberOfQuestions / THREE_LEVELS
                                && uiState.points <= (uiState.numberOfQuestions / THREE_LEVELS) * 2 -> {
                            getString(R.string.some_points, uiState.points)
                        }
                        uiState.points <= uiState.numberOfQuestions -> {
                            getString(R.string.many_points, uiState.points)
                        }
                        else -> ""
                    }
                }
            }
        }
        playButton.apply {
            isVisible =
                uiState.questionNumber == null
                        || uiState.question == null
            if (isVisible) {
                text = if (uiState.points == null) {
                    getString(R.string.yeah)
                } else {
                    getString(R.string.play_again)
                }
                if (uiState.onRetrieveQuestion != null && !hasOnClickListeners()) {
                    setOnClickListener {
                        uiState.onRetrieveQuestion.invoke()
                    }
                }
            }
        }
    }

    private fun shouldPopulateQuestionScreen(uiState: UiState) = uiState.run {
        if (question != null && choiceUiStateList != null) {
            populateQuestionViews(
                question,
                questionNumber!!,
                numberOfQuestions!!
            )
            populateChoices(choiceUiStateList)

            if (timeProgressPercentage != null && isCorrectChoice == null) {
                setProgressBar(timeProgressPercentage)
            }
        }
    }

    private fun populateQuestionViews(
        question: Question,
        questionNum: Int,
        numberOfQuestions: Int
    ) = binding.run {
        questionNumber.text = getString(R.string.question_number, questionNum, numberOfQuestions)
        questionType.text = question.type.replaceFirstChar { it.uppercase() }
        questionImage.loadUrl(question.image)
        questionText.text =
            HtmlCompat.fromHtml(question.question, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    private fun populateChoices(choiceUiStateList: List<MainViewModel.ChoiceUiState>) {
        adapter.submitList(choiceUiStateList)
    }

    private fun setProgressBar(timeProgressPercentage: Int) {
        binding.remainingTime.setProgressCompat(timeProgressPercentage, true)
    }

    private fun shouldInitContinueButtonListenerToRetrieveNextQuestion(onRetrieveQuestion: (() -> Unit)?) {
        if (onRetrieveQuestion != null && !binding.continueButton.hasOnClickListeners()) {
            initListenerToRetrieveQuestion(onRetrieveQuestion)
        }
    }

    private fun initListenerToRetrieveQuestion(onRetrieveQuestion: () -> Unit) {
        binding.continueButton.setOnClickListener {
            onRetrieveQuestion()
        }
    }

    private fun shouldShowContinueButton(correctChoice: Boolean?) = binding.run {
        continueButton.isVisible = correctChoice != null
        remainingTime.isVisible = correctChoice == null && !message.isVisible
    }

    private fun shouldShowErrorRetrieved(errorRetrieved: ErrorRetrieved?, onRetry: (() -> Unit)?) =
        binding.run {
            if (message.isVisible) {
                message.isVisible = errorRetrieved == null
                playButton.isVisible = errorRetrieved == null
            } else {
                questionNumber.isVisible = errorRetrieved == null
                questionType.isVisible = errorRetrieved == null
                questionImage.isVisible = errorRetrieved == null
                questionText.isVisible = errorRetrieved == null
                choiceList.isVisible = errorRetrieved == null

                if (remainingTime.isVisible) {
                    remainingTime.isVisible = errorRetrieved == null
                }
            }
            errorText.apply {
                isVisible = errorRetrieved != null
                text = errorRetrieved?.errorToString()
            }
            tryAgain.apply {
                isVisible = errorRetrieved != null
                if (onRetry != null && !hasOnClickListeners()) setOnClickListener { onRetry() }
            }
        }

    private fun ErrorRetrieved.errorToString(): String = when (this) {
        ErrorRetrieved.Connectivity -> getString(R.string.connectivity_error)
        is ErrorRetrieved.Server -> getString(R.string.server_error) + code
        is ErrorRetrieved.Unknown -> getString(R.string.unknown_error) + message
    }

    companion object {
        private const val THREE_LEVELS = 3
    }
}
