package sk.ikundlakova.scratchcardapp.ui.screen.scratch.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.ikundlakova.scratchcardapp.ui.screen.scratch.CardStatus
import sk.ikundlakova.scratchcardapp.ui.theme.ScratchCardAppTheme

@Composable
fun ScratchCard(
    cardStatus: CardStatus,
    goToActivationScreen: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .size(250.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        when (cardStatus) {
            CardStatus.New -> {
                Text(
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "New scratching card...",
                    style = MaterialTheme.typography.bodyLarge
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "?",
                        fontSize = 64.sp
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "Curious what's inside? ",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            CardStatus.ScratchingInProgress -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "Scratching... Please wait",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            is CardStatus.Scratched -> {
                val currentUuid = cardStatus.cardCode
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        modifier = Modifier
                            .padding(24.dp)
                            .align(Alignment.CenterHorizontally),
                        text = "You nailed it! Activate your card with the code:",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = currentUuid,
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = { goToActivationScreen() }
                    ) {
                        Text(
                            text = "Go to activation screen",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun ScratchCardPreviewNewCard() {
    ScratchCardAppTheme {
        ScratchCard(
            cardStatus = CardStatus.New
        )
    }
}
@Preview
@Composable
fun ScratchCardPreviewScratchInProgress() {
    ScratchCardAppTheme {
        ScratchCard(
            cardStatus = CardStatus.ScratchingInProgress
        )
    }
}
@Preview
@Composable
fun ScratchCardPreviewCardScratched() {
    ScratchCardAppTheme {
        ScratchCard(
            cardStatus = CardStatus.Scratched("123456789")
        )
    }
}