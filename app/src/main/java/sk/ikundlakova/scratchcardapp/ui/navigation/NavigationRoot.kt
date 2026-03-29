package sk.ikundlakova.scratchcardapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import sk.ikundlakova.scratchcardapp.ui.screen.activation.ActivationScreenRoot
import sk.ikundlakova.scratchcardapp.ui.screen.main.MainScreenRoot
import sk.ikundlakova.scratchcardapp.ui.screen.scratch.ScratchScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Any,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Screen.Main> {
            MainScreenRoot(
                onNavigateToScratch = { navController.navigate(Screen.Scratch) },
                onNavigateToActivation = { navController.navigate(Screen.Activation) }
            )
        }
        composable<Screen.Scratch> {
            ScratchScreenRoot(
                onNavigateBack = { navController.popBackStack() },
                onGoToActivationScreen = {
                    navController.popBackStack()
                    navController.navigate(Screen.Activation)
                }
            )
        }
        composable<Screen.Activation> {
            ActivationScreenRoot(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}