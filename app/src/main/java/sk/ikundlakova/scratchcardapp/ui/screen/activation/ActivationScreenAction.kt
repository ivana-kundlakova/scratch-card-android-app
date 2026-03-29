package sk.ikundlakova.scratchcardapp.ui.screen.activation

sealed interface ActivationScreenAction {
    data object OnActivateClick: ActivationScreenAction
    data object OnNavigateBackClick: ActivationScreenAction
    data object OnDismissErrorDialog: ActivationScreenAction
}