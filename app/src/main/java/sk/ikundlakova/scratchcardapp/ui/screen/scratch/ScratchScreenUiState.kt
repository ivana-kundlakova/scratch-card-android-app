package sk.ikundlakova.scratchcardapp.ui.screen.scratch

import sk.ikundlakova.scratchcardapp.ui.screen.scratch.CardStatus
import sk.ikundlakova.scratchcardapp.ui.screen.activation.CardActivationState

data class ScratchScreenUiState(
    val cardStatus: CardStatus = CardStatus.New,
    val scratchBtnEnabled: Boolean = true,
    val activationState: CardActivationState = CardActivationState.NotScratched
)
