package com.developer.lamarao.soletra.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.lamarao.soletra.database.model.Game
import com.developer.lamarao.soletra.home.usecase.GetGameUseCase
import com.developer.lamarao.soletra.home.usecase.StartNewGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGameUseCase: GetGameUseCase,
    private val startNewGameUseCase: StartNewGameUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenUiState())
    val viewState = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    private fun checkIfGameIsRunning() {
        viewModelScope.launch {
            val currentGame = getGameUseCase.getCurrentGame()
            _state.update { state ->
                state.copy(currentGame = currentGame)
            }
        }
    }

    fun onContinueGameClicked() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToGame(_state.value.currentGame))
        }
    }

    fun onNewGameClicked() {
        viewModelScope.launch {
            startNewGameUseCase.startNewGame(_state.value.currentGame)
            _uiEvent.emit(UiEvent.NavigateToGame(null))
        }
    }

    fun onResume() {
        checkIfGameIsRunning()
    }

    data class HomeScreenUiState(
        val currentGame: Game? = null
    )

    sealed class UiEvent {
        data class NavigateToGame(val game: Game?) : UiEvent()
    }
}