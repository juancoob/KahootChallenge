package com.juancoob.kahootchallenge.ui

import com.juancoob.domain.Choice
import com.juancoob.kahootchallenge.ui.MainViewModel.ChoiceUiState
import javax.inject.Inject

class ChoiceUiStateMapper @Inject constructor() {

    fun Choice.toChoiceUiState(block: () -> Unit): ChoiceUiState = ChoiceUiState(
        choice = this,
        onClickChoice = block
    )
}
