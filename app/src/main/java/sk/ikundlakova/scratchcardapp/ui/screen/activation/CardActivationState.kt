package sk.ikundlakova.scratchcardapp.ui.screen.activation

sealed interface CardActivationState {
    data object NotScratched : CardActivationState
    data class ReadyToActivate(val cardCode: String) : CardActivationState
    data object Activating : CardActivationState
    data object Success : CardActivationState
    data class Error(val message: String) : CardActivationState
}