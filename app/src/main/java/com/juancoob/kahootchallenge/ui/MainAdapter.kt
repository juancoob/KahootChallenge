package com.juancoob.kahootchallenge.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.juancoob.kahootchallenge.R
import com.juancoob.kahootchallenge.common.loadBackground
import com.juancoob.kahootchallenge.common.loadDrawable
import com.juancoob.kahootchallenge.databinding.QuizItemBinding
import com.juancoob.kahootchallenge.ui.MainViewModel.ChoiceUiState

class MainAdapter : ListAdapter<ChoiceUiState, MainAdapter.ViewHolder>(object :
    DiffUtil.ItemCallback<ChoiceUiState>() {
    override fun areItemsTheSame(oldItem: ChoiceUiState, newItem: ChoiceUiState): Boolean =
        oldItem.choice.text == newItem.choice.text

    override fun areContentsTheSame(oldItem: ChoiceUiState, newItem: ChoiceUiState): Boolean =
        oldItem.choice == newItem.choice
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QuizItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val choiceUiState = getItem(position)
        val choiceUiStatePosition = currentList.indexOf(choiceUiState)
        when {
            choiceUiStatePosition % 2 == 0 -> {
                if (choiceUiStatePosition % FOUR_CHOICES == 0) {
                    holder.populateChoice(
                        choiceUiState,
                        choiceUiStatePosition,
                        R.drawable.background_red_answer_button,
                        R.drawable.ic_triangle
                    )
                } else {
                    holder.populateChoice(
                        choiceUiState,
                        choiceUiStatePosition,
                        R.drawable.background_orange_answer_button,
                        R.drawable.ic_circle
                    )
                }
            }
            choiceUiStatePosition % 2 != 0 -> {
                if ((choiceUiStatePosition - 1) % FOUR_CHOICES == 0) {
                    holder.populateChoice(
                        choiceUiState,
                        choiceUiStatePosition,
                        R.drawable.background_blue_answer_button,
                        R.drawable.ic_diamond
                    )
                } else {
                    holder.populateChoice(
                        choiceUiState,
                        choiceUiStatePosition,
                        R.drawable.background_green_answer_button,
                        R.drawable.ic_square
                    )
                }
            }
        }
    }

    class ViewHolder(private val binding: QuizItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun populateChoice(
            choiceUiState: ChoiceUiState,
            choiceUiStatePosition: Int,
            @DrawableRes choiceBackgroundId: Int,
            @DrawableRes polygonDrawableId: Int
        ) = binding.run {

            choice.text = choiceUiState.choice.text
            when {
                !choiceUiState.choice.showAnswer -> {
                    choice.apply {
                        loadBackground(choiceBackgroundId)
                        setOnClickListener {
                            choiceUiState.onClickChoice()
                            setOnClickListener(null)
                        }
                    }
                    polygonImage.loadDrawable(polygonDrawableId)
                }
                choiceUiState.choice.showAnswer && choiceUiStatePosition % 2 == 0 -> {
                    showChoiceTypes(choiceUiState, choice, leftChoiceType)
                }
                choiceUiState.choice.showAnswer && choiceUiStatePosition % 2 != 0 -> {
                    showChoiceTypes(choiceUiState, choice, rightChoiceType)
                }
            }
            setUIVisibility(choiceUiState.choice.showAnswer, choiceUiStatePosition)
        }

        private fun showChoiceTypes(
            choiceUiState: ChoiceUiState,
            choice: TextView,
            choiceType: ImageView
        ) {
            when {
                choiceUiState.choice.isSelected && !choiceUiState.choice.isCorrect -> {
                    choice.loadBackground(R.drawable.background_wrong_answer_button)
                    choiceType.loadDrawable(R.drawable.ic_wrong)
                }
                !choiceUiState.choice.isSelected && !choiceUiState.choice.isCorrect -> {
                    choice.loadBackground(R.drawable.background_default_wrong_answer_button)
                    choiceType.loadDrawable(R.drawable.ic_wrong)
                }
                choiceUiState.choice.isCorrect -> {
                    choice.loadBackground(R.drawable.background_correct_answer_button)
                    choiceType.loadDrawable(R.drawable.ic_correct)
                }
            }
        }

        private fun setUIVisibility(showAnswer: Boolean, choiceUiStatePosition: Int) = binding.run {
            polygonImage.isVisible = !showAnswer
            leftChoiceType.isVisible = showAnswer && choiceUiStatePosition % 2 == 0
            rightChoiceType.isVisible = showAnswer && choiceUiStatePosition % 2 != 0
        }
    }

    companion object {
        private const val FOUR_CHOICES = 4
    }
}
