package sk.ikundlakova.scratchcardapp.ui.screen.main

interface MainScreenAction {
    data object OnNavigateToScratchClick: MainScreenAction
    data object OnNavigateToActivationClick: MainScreenAction
}