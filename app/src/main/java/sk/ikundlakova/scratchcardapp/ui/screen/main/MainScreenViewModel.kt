package sk.ikundlakova.scratchcardapp.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sk.ikundlakova.scratchcardapp.domain.model.CardRepository
import sk.ikundlakova.scratchcardapp.ui.screen.activation.CardActivationState

class MainScreenViewModel(
    private val cardRepository: CardRepository
): ViewModel() {


    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            cardRepository.getCurrentCardStateFlow().collect { cardActivationState ->
                _uiState.update {
                    it.copy(
                        cardStatusDescription = when (cardActivationState) {
                            CardActivationState.Activating -> "Activating your card... Please wait..."
                            is CardActivationState.Error -> "Whoops! Something went wrong."
                            CardActivationState.NotScratched -> "You have a new card waiting for you to scratch. Go on, click on the \"Go to Scratch Screen\" button below!"
                            is CardActivationState.ReadyToActivate -> "Your card with code \n${cardActivationState.cardCode} \nis ready to be activated! Please click on the \"Go to Activation Screen\" button below."
                            CardActivationState.Success -> "Your card is activated successfully! \nReady to scratch again?"
                        }
                    )
                }
            }
        }
    }

    fun onAction(action: MainScreenAction) {
        when (action) {
            else -> Unit
        }

    }

}