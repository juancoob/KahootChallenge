package com.juancoob.kahootchallenge

import com.juancoob.kahootchallenge.ui.ChoiceUiStateMapper
import com.juancoob.kahootchallenge.ui.MainViewModel
import com.juancoob.testshared.mockedChoice
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ChoiceUiStateMapperTest {

    @MockK
    private lateinit var lambda: () -> Unit

    @MockK
    private lateinit var choiceUiStateMapper: ChoiceUiStateMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `The choice data model is mapped to the UI model to show the current UI state`() {
        val expectedResult = MainViewModel.ChoiceUiState(
            choice = mockedChoice,
            onClickChoice = lambda
        )
        with(choiceUiStateMapper) {
            every { mockedChoice.toChoiceUiState(any()) } returns MainViewModel.ChoiceUiState(
                choice = mockedChoice,
                onClickChoice = lambda
            )
        }
        val actualResult = with(choiceUiStateMapper) { mockedChoice.toChoiceUiState(lambda) }
        assertEquals(expectedResult, actualResult)
    }
}
