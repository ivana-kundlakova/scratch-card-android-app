package sk.ikundlakova.scratchcardapp.ui.screen.scratch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sk.ikundlakova.scratchcardapp.domain.model.CardRepository
import sk.ikundlakova.scratchcardapp.ui.screen.activation.CardActivationState
import java.util.UUID

class ScratchScreenViewModel(
    private val cardRepository: CardRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ScratchScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: ScratchScreenAction) {
        when (action) {
            ScratchScreenAction.OnScratchClick -> scratchCard()
            else -> Unit
        }
    }

    private fun scratchCard() {

        _uiState.value = _uiState.value.copy(
            cardStatus = CardStatus.ScratchingInProgress,
            scratchBtnEnabled = false
        )

        viewModelScope.launch(Dispatchers.Default) {

            val cardCode = UUID.randomUUID().toString()

            delay(2_000)

            ensureActive()

            cardRepository.saveCurrentCardState(
                CardActivationState.ReadyToActivate(
                    cardCode = cardCode
                )
            )
            _uiState.value = _uiState.value.copy(
                cardStatus = CardStatus.Scratched(cardCode = cardCode)
            )
        }

    }
}
