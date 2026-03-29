package sk.ikundlakova.scratchcardapp.ui.screen.activation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import sk.ikundlakova.scratchcardapp.ui.theme.ScratchCardAppTheme

@Composable
fun ActivationScreenRoot(
    viewModel: ActivationScreenViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showErrorDialog by viewModel.showErrorDialog.collectAsStateWithLifecycle()

    ActivationScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                ActivationScreenAction.OnNavigateBackClick -> onNavigateBack()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        showErrorDialog = showErrorDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivationScreen(
    uiState: ActivationScreenUiState,
    onAction: (ActivationScreenAction) -> Unit,
    showErrorDialog: Boolean = false
) {
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                onAction(ActivationScreenAction.OnDismissErrorDialog)
            },
            title = {
                Text("Activation Error")
                    },
            text = { Text(
                text =
                    if (uiState.activationState is CardActivationState.Error) uiState.activationState.message
                    else "Something went wrong...",
                style = MaterialTheme.typography.bodyMedium
            ) },
            confirmButton = {
                TextButton(onClick = {
                    onAction(ActivationScreenAction.OnDismissErrorDialog)
                }) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        text = "OK",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activate Card") },
                navigationIcon = {
                    IconButton(onClick = {
                        onAction(ActivationScreenAction.OnNavigateBackClick)
                    }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (uiState.activationState) {
                    is CardActivationState.Error -> {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Whoops! Something went wrong..",
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onAction(ActivationScreenAction.OnActivateClick) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                text = "TRY AGAIN",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                    CardActivationState.Activating -> {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Activating...",
                            textAlign = TextAlign.Center
                        )
                    }
                    CardActivationState.NotScratched ->{
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "No card to activate yet...\nPlease scratch your card first.",
                            textAlign = TextAlign.Center
                        )
                    }
                    is CardActivationState.ReadyToActivate -> {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Your card is ready to Activate!",
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Card code: ${uiState.activationState.cardCode}",
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onAction(ActivationScreenAction.OnActivateClick) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                text = "ACTIVATE NOW",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                    CardActivationState.Success -> {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Yahoo! Card activated.\nLet´s do this again! Go to scratch screen..",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ActivationScreenNotScratchedPreview() {
    ScratchCardAppTheme {
        ActivationScreen(
            uiState = ActivationScreenUiState(
                activationState = CardActivationState.NotScratched
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun ActivationScreenReadyToActivatePreview() {
    ScratchCardAppTheme {
        ActivationScreen(
            uiState = ActivationScreenUiState(
                activationState = CardActivationState.ReadyToActivate("123456")
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun ActivationScreenActivatingPreview() {
    ScratchCardAppTheme {
        ActivationScreen(
            uiState = ActivationScreenUiState(
                activationState = CardActivationState.Activating
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun ActivationScreenSuccessPreview() {
    ScratchCardAppTheme {
        ActivationScreen(
            uiState = ActivationScreenUiState(
                activationState = CardActivationState.Success
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun ActivationScreenErrorPreview() {
    ScratchCardAppTheme {
        ActivationScreen(
            uiState = ActivationScreenUiState(
                activationState = CardActivationState.Error("Something went wrong...")
            ),
            onAction = {},
            showErrorDialog = true
        )
    }
}
