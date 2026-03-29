package sk.ikundlakova.scratchcardapp.ui.screen.scratch

sealed interface ScratchScreenAction {

    data object OnScratchClick: ScratchScreenAction
    data object OnNavigateBack: ScratchScreenAction
    data object OnGoToActivationScreenClick: ScratchScreenAction

}