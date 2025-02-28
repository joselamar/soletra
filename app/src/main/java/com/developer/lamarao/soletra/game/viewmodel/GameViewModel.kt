package com.developer.lamarao.soletra.game.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.lamarao.soletra.R
import com.developer.lamarao.soletra.core.ui.replace
import com.developer.lamarao.soletra.database.model.Game
import com.developer.lamarao.soletra.database.model.Word
import com.developer.lamarao.soletra.game.usecase.CreateGameUseCase
import com.developer.lamarao.soletra.game.usecase.UpdateGameUseCase
import com.developer.lamarao.soletra.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createGameUseCase: CreateGameUseCase,
    private val updateGameUseCase: UpdateGameUseCase
) : ViewModel() {

    private val args: GameScreenArgs = savedStateHandle.navArgs()
    private val _state = MutableStateFlow(GameScreenUiState(args.game))
    val viewState = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    init {
        if (args.game == null) {
            viewModelScope.launch(Dispatchers.IO) {
                _state.update { state ->
                    state.copy(
                        currentGame = createGameUseCase.createGame()
                    )
                }
            }
        }
    }

    fun onHexagonClicked(letter: String) {
        _state.update { state ->
            if ((state.currentGame?.currentGuess?.length ?: 0) >= 8) return
            state.copy(
                currentGame = state.currentGame?.copy(currentGuess = state.currentGame.currentGuess + letter)
            )
        }
    }

    fun saveGame() {
        viewModelScope.launch {
            _state.value.currentGame?.let { game -> updateGameUseCase.updateGame(game) }
        }
    }

    fun onBackClicked() {
        saveGame()
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateBack)
        }
    }

    fun onDeleteClicked() {
        _state.update { state ->
            state.copy(
                currentGame = state.currentGame?.copy(
                    currentGuess = state.currentGame.currentGuess.dropLast(1)
                )
            )
        }
    }

    fun onShuffledClicked() {
        _state.update { state ->
            state.copy(
                currentGame = state.currentGame?.copy(
                    remainingLetters = state.currentGame.remainingLetters.shuffled()
                )
            )
        }
    }

    fun onConfirmClicked() {
        val currentGuess = _state.value.currentGame?.currentGuess.orEmpty()
        val wordList = _state.value.currentGame?.words
        when {
            currentGuess.length <= 3 -> {
                viewModelScope.launch {
                    _uiEvent.emit(UiEvent.ShowSnackBar(R.string.snackbar_message_small_guess))
                }
            }

            wordList?.contains(Word(currentGuess)) == true -> {
                _state.update { state ->
                    state.copy(
                        currentGame =
                        state.currentGame?.let { game ->
                            game.copy(
                                words = game.words.replace(
                                    newValue = Word(
                                        currentGuess,
                                        isDiscovered = true
                                    )
                                ) { oldValue -> oldValue == Word(currentGuess) },
                                score = game.score + 1
                            )
                        }
                    )
                }
                saveGame()
            }

            wordList?.contains(Word(currentGuess, isDiscovered = true)) == true -> {
                viewModelScope.launch {
                    _uiEvent.emit(UiEvent.ShowSnackBar(R.string.snackbar_message_same_guess))
                }
            }

            else -> viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowSnackBar(R.string.snackbar_message_not_in_words))
            }
        }
        _state.update { state ->
            state.copy(
                currentGame = state.currentGame?.copy(
                    currentGuess = ""
                )
            )
        }
    }

    fun onHelpClicked() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowInstructions)
        }
    }

    fun onHintClicked() {
        _state.update { state ->
            state.copy(
                currentGame =
                state.currentGame?.let { game ->
                    val notRevealedWords = game.words.filter { word -> word.isDiscovered == false }
                    val randomWord = notRevealedWords.randomOrNull() ?: return
                    game.copy(
                        words = game.words.replace(
                            newValue = Word(
                                randomWord.text,
                                isDiscovered = true
                            )
                        ) { oldValue -> oldValue == Word(randomWord.text) },
                        score = game.score + 1
                    )
                }
            )
        }
    }

    data class GameScreenUiState(
        val currentGame: Game? = null
    )

    sealed class UiEvent {
        data object NavigateBack : UiEvent()
        data class ShowSnackBar(val stringId: Int) : UiEvent()
        data object ShowInstructions : UiEvent()
    }
}