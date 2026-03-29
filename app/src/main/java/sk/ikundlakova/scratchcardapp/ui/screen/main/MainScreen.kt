package sk.ikundlakova.scratchcardapp.ui.screen.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import sk.ikundlakova.scratchcardapp.ui.theme.ScratchCardAppTheme

@Composable
fun MainScreenRoot(
    viewModel: MainScreenViewModel = koinViewModel(),
    onNavigateToScratch: () -> Unit,
    onNavigateToActivation: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                MainScreenAction.OnNavigateToScratchClick -> { onNavigateToScratch() }
                MainScreenAction.OnNavigateToActivationClick -> { onNavigateToActivation() }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun MainScreen(
    uiState: MainScreenUiState,
    onAction: (MainScreenAction) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Text(
                text = "Scratch Card Status",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = uiState.cardStatusDescription,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onAction(MainScreenAction.OnNavigateToScratchClick) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text ="Go to Scratch Screen",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Button(
                onClick = { onAction(MainScreenAction.OnNavigateToActivationClick) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text = "Go to Activation Screen",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    ScratchCardAppTheme {
        MainScreen(
            uiState = MainScreenUiState(
                cardStatusDescription = "NEW"
            ),
            onAction = {}
        )
    }
}

