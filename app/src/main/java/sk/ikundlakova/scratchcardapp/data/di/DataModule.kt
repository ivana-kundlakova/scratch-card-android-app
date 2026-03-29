package sk.ikundlakova.scratchcardapp.data.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import sk.ikundlakova.scratchcardapp.data.api.KtorActivationService
import sk.ikundlakova.scratchcardapp.data.card.CardRepositoryImpl
import sk.ikundlakova.scratchcardapp.domain.model.CardRepository
import sk.ikundlakova.scratchcardapp.domain.networking.ActivationService

val dataModule = module {
    single {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    single<ActivationService> {
        KtorActivationService()
    }

    single<CardRepository> {
        CardRepositoryImpl(
            context = get(),
            coroutineScope = get(),
            ioDispatcher = Dispatchers.IO,
            activationService = get()
        )
    }
}