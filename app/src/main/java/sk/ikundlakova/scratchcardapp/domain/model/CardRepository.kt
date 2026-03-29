package sk.ikundlakova.scratchcardapp.domain.model

import kotlinx.coroutines.flow.Flow
import sk.ikundlakova.scratchcardapp.domain.networking.util.DataError
import sk.ikundlakova.scratchcardapp.ui.screen.activation.CardActivationState

interface CardRepository {
    fun activateCard(code: String, onSuccess: () -> Unit, onError: (DataError.Remote) -> Unit)
    fun getCurrentCardStateFlow(): Flow<CardActivationState>
    suspend fun saveCurrentCardState(state: CardActivationState)
}