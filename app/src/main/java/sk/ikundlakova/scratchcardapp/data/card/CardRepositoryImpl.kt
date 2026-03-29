package sk.ikundlakova.scratchcardapp.data.card

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.ikundlakova.scratchcardapp.domain.model.CardRepository
import sk.ikundlakova.scratchcardapp.domain.networking.ActivationService
import sk.ikundlakova.scratchcardapp.domain.networking.util.DataError
import sk.ikundlakova.scratchcardapp.domain.networking.util.onFailure
import sk.ikundlakova.scratchcardapp.domain.networking.util.onSuccess
import sk.ikundlakova.scratchcardapp.ui.screen.activation.CardActivationState

val Context.dataStore by preferencesDataStore("scratch_card_app")

class CardRepositoryImpl(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher,
    private val activationService: ActivationService
): CardRepository {

    private val CURRENT_CARD_KEY = stringPreferencesKey("current_card")
    private val CURRENT_CARD_ACTIVATION_STATUS_KEY = stringPreferencesKey("current_card_activation_status")

    override fun activateCard(code: String, onSuccess: () -> Unit, onError: (DataError.Remote) -> Unit) {
        coroutineScope.launch(ioDispatcher) {
            withContext(NonCancellable) {
                activationService.activateCard(code)
                    .onSuccess { response ->
                        Log.d("CardRepositoryImpl", "activateCard: $response")
                        val value = response.android?.toIntOrNull()
                        if (value != null && value > 277028 ) {
                            saveCurrentCardState(CardActivationState.Success)
                            onSuccess()
                        }
                        else onError(DataError.Remote.VERY_SPECIFIC_ERROR)
                    }.onFailure { onError(it) }
            }
        }
    }

    override fun getCurrentCardStateFlow(): Flow<CardActivationState> {
        return context.dataStore.data.map { preferences ->
            val cardCode = preferences[CURRENT_CARD_KEY]
            val activationStatus = preferences[CURRENT_CARD_ACTIVATION_STATUS_KEY]
            when (activationStatus) {
                "READY_TO_ACTIVATE" if cardCode != null -> CardActivationState.ReadyToActivate(cardCode)
                "ACTIVATING" -> CardActivationState.Activating
                "SUCCESS" -> CardActivationState.Success
                "ERROR" -> CardActivationState.Error("Failed to activate card..")
                else -> CardActivationState.NotScratched
            }

        }.distinctUntilChanged()
    }

    override suspend fun saveCurrentCardState(state: CardActivationState) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit {
                when (state) {
                    is CardActivationState.ReadyToActivate -> {
                        it[CURRENT_CARD_KEY] = state.cardCode
                        it[CURRENT_CARD_ACTIVATION_STATUS_KEY] = "READY_TO_ACTIVATE"
                    }
                    CardActivationState.Activating -> {
                        it[CURRENT_CARD_ACTIVATION_STATUS_KEY] = "ACTIVATING"
                    }
                    is CardActivationState.Error -> {
                        it[CURRENT_CARD_ACTIVATION_STATUS_KEY] = "ERROR"
                    }
                    CardActivationState.NotScratched -> {
                        it.remove(CURRENT_CARD_KEY)
                        it.remove(CURRENT_CARD_ACTIVATION_STATUS_KEY)
                    }
                    CardActivationState.Success -> {
                        it[CURRENT_CARD_ACTIVATION_STATUS_KEY] = "SUCCESS"
                    }
                }
            }
        }
    }
}