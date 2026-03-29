package sk.ikundlakova.scratchcardapp.ui.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import sk.ikundlakova.scratchcardapp.ui.screen.activation.ActivationScreenViewModel
import sk.ikundlakova.scratchcardapp.ui.screen.main.MainScreenViewModel
import sk.ikundlakova.scratchcardapp.ui.screen.scratch.ScratchScreenViewModel

val uiModule = module {
    viewModelOf(::MainScreenViewModel)
    viewModelOf(::ActivationScreenViewModel)
    viewModelOf(::ScratchScreenViewModel)
}