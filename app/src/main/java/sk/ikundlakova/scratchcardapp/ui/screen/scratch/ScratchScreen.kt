package sk.ikundlakova.scratchcardapp.ui.screen.scratch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import sk.ikundlakova.scratchcardapp.ui.screen.scratch.CardStatus
import sk.ikundlakova.scratchcardapp.ui.screen.scratch.components.ScratchCard
import sk.ikundlakova.scratchcardapp.ui.theme.ScratchCardAppTheme

@Composable
fun ScratchScreenRoot(
    viewModel: ScratchScreenViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onGoToActivationScreen: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScratchScreen(
        uiState = uiState,
        onAction = {
            when (it) {
                ScratchScreenAction.OnNavigateBack -> { onNavigateBack() }
                ScratchScreenAction.OnGoToActivationScreenClick -> { onGoToActivationScreen() }
                else -> Unit
            }
            viewModel.onAction(it)
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScratchScreen(
    uiState: ScratchScreenUiState,
    onAction: (ScratchScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scratch Card") },
                navigationIcon = {
                    IconButton(onClick = { onAction(ScratchScreenAction.OnNavigateBack) }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 24.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            ScratchCard(
                cardStatus = uiState.cardStatus,
                goToActivationScreen = { onAction(ScratchScreenAction.OnGoToActivationScreenClick) }
            )
            Button(
                onClick = { onAction(ScratchScreenAction.OnScratchClick) },
                modifier = Modifier.width(250.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = uiState.scratchBtnEnabled
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "SCRATCH!",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }
}

@Preview
@Composable
fun ScratchScreenPreview() {
    ScratchCardAppTheme {
        ScratchScreen(
            uiState = ScratchScreenUiState(
                cardStatus = CardStatus.New
            ),
            onAction = {}
        )
    }
}