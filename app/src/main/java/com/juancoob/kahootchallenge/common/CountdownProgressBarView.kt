package com.juancoob.kahootchallenge.common

import android.animation.ObjectAnimator
import android.content.Context
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.juancoob.kahootchallenge.R
import com.juancoob.kahootchallenge.databinding.ProgressBarViewBinding

class CountdownProgressBarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {

    private val binding: ProgressBarViewBinding
    private var floatPercentage: Float = 0F
    private var screenMargin: Int = 0

    init {
        val time: Int
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.CountdownProgressBarView,
            0,
            0
        ).apply {
            try {
                time = getInteger(R.styleable.CountdownProgressBarView_time, 0)
            } finally {
                recycle()
            }
        }
        binding = ProgressBarViewBinding.inflate(LayoutInflater.from(context), this, true)
        binding.timeProgressText.text = time.toString()
    }

    fun updateCountdownProgressBar(
        timeInSeconds: Int,
        timeProgressPercentage: Int,
        screenWidth: Int
    ) {
        floatPercentage = (timeProgressPercentage / ONE_HUNDRED_PERCENT_FLOAT)

        binding.timeProgressBackground.apply {
            (layoutParams as ConstraintLayout.LayoutParams).matchConstraintPercentWidth =
                floatPercentage

            invalidate()
            requestLayout()
        }

        binding.timeProgressText.apply {
            text = timeInSeconds.toString()

            if (timeProgressPercentage.toFloat() == ONE_HUNDRED_PERCENT_FLOAT) {
                translationX = 0f
                isVisible = false
            } else {
                screenMargin = (screenWidth - binding.timeProgressWrappper.width) / 2
                ObjectAnimator.ofFloat(
                    this,
                    "translationX",
                    if(!isVisible) {
                        -screenMargin.toFloat()
                    } else {
                        (binding.timeProgressBackground.width - binding.timeProgressWrappper.width
                                - screenMargin).toFloat()
                    }
                ).apply {
                    interpolator = LinearInterpolator()
                    duration = ONE_SECOND_IN_MILLIS
                    isVisible = true
                    start()
                }
            }
            invalidate()
            requestLayout()
        }

        TransitionManager.beginDelayedTransition(
            binding.timeProgressWrappper,
            ChangeBounds().apply {
                interpolator = LinearInterpolator()
                duration = ONE_SECOND_IN_MILLIS
            }
        )

    }

    companion object {
        private const val ONE_SECOND_IN_MILLIS = 1000L
        private const val ONE_HUNDRED_PERCENT_FLOAT = 100f
    }
}
