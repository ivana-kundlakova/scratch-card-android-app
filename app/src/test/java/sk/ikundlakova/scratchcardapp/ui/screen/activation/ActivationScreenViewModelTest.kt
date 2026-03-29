package sk.ikundlakova.scratchcardapp.ui.screen.activation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import sk.ikundlakova.scratchcardapp.domain.model.CardRepository
import sk.ikundlakova.scratchcardapp.domain.networking.util.DataError

/**
 * Unit tests for [ActivationScreenViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ActivationScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ActivationScreenViewModel
    private lateinit var repository: FakeCardRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeCardRepository()
        viewModel = ActivationScreenViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Verifies that the success flow of card activation correctly transitions through
     * Activating and Success states, and re-enables the button as expected.
     */
    @Test
    fun `OnActivateClick success flow`() = runTest {
        // Collect uiState to keep the StateFlow active (due to WhileSubscribed behavior)
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }

        // Given: Card is ready to activate
        val testCode = "test-uuid"
        repository.updateState(CardActivationState.ReadyToActivate(testCode))
        advanceUntilIdle() // Process init block collection
        
        assertEquals(testCode, viewModel.uiState.value.cardCode)
        assertTrue(viewModel.uiState.value.activationBtnEnabled)

        // When: OnActivateClick is triggered
        viewModel.onAction(ActivationScreenAction.OnActivateClick)
        
        // Then: State transitions to Activating and button is disabled
        assertEquals(CardActivationState.Activating, viewModel.uiState.value.activationState)
        assertTrue(!viewModel.uiState.value.activationBtnEnabled)

        // And: Repository's activateCard is called with correct code
        assertEquals(testCode, repository.lastCode)

        // When: Repository signals success
        repository.triggerSuccess()
        advanceUntilIdle()

        // Then: Final state is Success
        assertEquals(CardActivationState.Success, viewModel.uiState.value.activationState)
        
        collectJob.cancel()
    }

    /**
     * Verifies that the activation process continues even if the ViewModel is no longer observed
     * (simulating the user navigating back from the screen).
     */
    @Test
    fun `activation process continues even if screen is closed (ViewModel no longer observed)`() = runTest {
        // 1. Start observing the UI state
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }

        // Given: Card is ready to activate
        repository.updateState(CardActivationState.ReadyToActivate("test-code"))
        advanceUntilIdle()

        // When: Activation is initiated
        viewModel.onAction(ActivationScreenAction.OnActivateClick)
        
        // Simulate "navigating back" by canceling the UI observation
        collectJob.cancel()

        // When: The repository operation completes (it was not canceled because it uses a non-VM scope)
        repository.triggerSuccess()
        advanceUntilIdle()

        // Then: The global card state in the repository should still be updated to Success
        assertEquals(CardActivationState.Success, repository.getCurrentCardStateFlow().first())
    }

    @Test
    fun `OnActivateClick error from API handling`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }

        // Given: Card is ready to activate
        repository.updateState(CardActivationState.ReadyToActivate("code"))
        advanceUntilIdle()

        // When: OnActivateClick is triggered and repository returns VERY_SPECIFIC_ERROR
        viewModel.onAction(ActivationScreenAction.OnActivateClick)
        repository.triggerError(DataError.Remote.VERY_SPECIFIC_ERROR)
        advanceUntilIdle()

        // Then: Error message is correct and button is re-enabled
        val currentState = viewModel.uiState.value.activationState
        assertTrue(currentState is CardActivationState.Error)
        assertTrue(viewModel.uiState.value.activationBtnEnabled)
        assertTrue(viewModel.showErrorDialog.value)

        collectJob.cancel()
    }

    @Test
    fun `OnDismissErrorDialog state update`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }

        // Given: Error dialog is shown
        repository.updateState(CardActivationState.ReadyToActivate("code"))
        advanceUntilIdle()
        viewModel.onAction(ActivationScreenAction.OnActivateClick)
        repository.triggerError(DataError.Remote.UNKNOWN)
        advanceUntilIdle()
        assertTrue(viewModel.showErrorDialog.value)

        // When: OnDismissErrorDialog is triggered
        viewModel.onAction(ActivationScreenAction.OnDismissErrorDialog)
        advanceUntilIdle()

        // Then: Dialog is hidden
        assertTrue(!viewModel.showErrorDialog.value)

        collectJob.cancel()
    }

    /**
     * A fake implementation of [CardRepository] for testing purposes.
     */
    class FakeCardRepository : CardRepository {
        private val _cardFlow = MutableStateFlow<CardActivationState>(CardActivationState.NotScratched)

        override fun getCurrentCardStateFlow(): Flow<CardActivationState> = _cardFlow

        override suspend fun saveCurrentCardState(state: CardActivationState) {
            _cardFlow.value = state
        }

        fun updateState(state: CardActivationState) {
            _cardFlow.update { state }
        }

        var lastCode: String? = null
        private var successCallback: (() -> Unit)? = null
        private var errorCallback: ((DataError.Remote) -> Unit)? = null

        override fun activateCard(code: String, onSuccess: () -> Unit, onError: (DataError.Remote) -> Unit) {
            lastCode = code
            successCallback = onSuccess
            errorCallback = onError
        }

        fun triggerSuccess() {
            // In the real implementation, the repository updates the global state AND calls the callback.
            _cardFlow.value = CardActivationState.Success
            successCallback?.invoke()
        }

        fun triggerError(error: DataError.Remote) {
            errorCallback?.invoke(error)
        }
    }
}
