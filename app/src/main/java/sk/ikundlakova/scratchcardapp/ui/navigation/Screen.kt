package sk.ikundlakova.scratchcardapp.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Main : Screen
    @Serializable
    data object Scratch : Screen
    @Serializable
    data  object Activation : Screen
}
