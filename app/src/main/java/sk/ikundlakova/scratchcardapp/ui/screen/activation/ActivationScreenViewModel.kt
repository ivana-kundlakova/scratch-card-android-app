package sk.ikundlakova.scratchcardapp.ui.screen.activation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sk.ikundlakova.scratchcardapp.domain.model.CardRepository
import sk.ikundlakova.scratchcardapp.domain.networking.util.DataError

class ActivationScreenViewModel(
    private val cardRepository: CardRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ActivationScreenUiState())
    val uiState: StateFlow<ActivationScreenUiState> = _uiState.asStateFlow()

    private val _showErrorDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    init {
        viewModelScope.launch {
            cardRepository.getCurrentCardStateFlow().collect { cardActivationState ->
                when (cardActivationState) {
                    is CardActivationState.ReadyToActivate -> {
                        _uiState.update {
                            it.copy(
                                cardCode = cardActivationState.cardCode,
                                activationState = cardActivationState,
                                activationBtnEnabled = true
                            )
                        }
                    }
                    is CardActivationState.Error -> {
                        _uiState.update {
                            it.copy(
                                cardCode = null,
                                activationState = cardActivationState,
                            )
                        }
                    }
                    else -> {
                        _uiState.update {
                            it.copy(
                                cardCode = null,
                                activationState = cardActivationState
                            )
                        }
                    }

                }
            }
        }
    }

    fun onAction(action: ActivationScreenAction) {
        when (action) {
            is ActivationScreenAction.OnActivateClick -> activateCard()
            ActivationScreenAction.OnDismissErrorDialog -> _showErrorDialog.update { false }
            else -> Unit
        }
    }

    private fun activateCard() {
        _uiState.update { it.copy(
            activationState = CardActivationState.Activating,
            activationBtnEnabled = false
        ) }
        val cardCode = _uiState.value.cardCode

        if (cardCode == null) {
            _uiState.update {
                it.copy(
                    activationState = CardActivationState.Error("Ups, something went wrong... No card to activate"),
                )
            }
            return
        }

        cardRepository.activateCard(
            code = cardCode,
            onSuccess = {
                _uiState.update { it.copy(
                    activationState = CardActivationState.Success,
                ) }
            },
            onError = { error ->
                _uiState.update { it.copy(
                    activationState = CardActivationState.Error(
                        when(error) {
                            DataError.Remote.VERY_SPECIFIC_ERROR -> "Some very ugly error occurred.. \nIt seems like the answer from the server is not as expected.\nYou can try again, or just give up."
                            DataError.Remote.NO_INTERNET_CONNECTION -> "No internet connection. \nPlease check your connection and try again."
                            else -> "Ups, something went wrong, but the developer is too lazy to tell you, what exactly.."
                        }
                    ),
                    activationBtnEnabled = true
                ) }
                _showErrorDialog.update { true }
            }
        )
    }

}