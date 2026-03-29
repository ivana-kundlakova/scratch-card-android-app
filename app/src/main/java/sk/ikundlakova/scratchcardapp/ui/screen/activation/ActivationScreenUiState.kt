package sk.ikundlakova.scratchcardapp.ui.screen.activation

data class ActivationScreenUiState(
    val cardCode: String? = null,
    val activationState: CardActivationState = CardActivationState.NotScratched,
    val activationBtnEnabled: Boolean = true,
)
