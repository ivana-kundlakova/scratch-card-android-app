package sk.ikundlakova.scratchcardapp.ui.screen.scratch

sealed interface CardStatus {
    data object New : CardStatus
    data object ScratchingInProgress : CardStatus
    data class Scratched(val cardCode: String) : CardStatus
}