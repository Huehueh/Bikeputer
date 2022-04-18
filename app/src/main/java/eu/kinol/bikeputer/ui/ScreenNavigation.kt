package eu.kinol.bikeputer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eu.kinol.bikeputer.values.Constants

@ExperimentalAnimationApi
@Composable
fun ScreenNavigation(
    navController: NavHostController,
    bikeData: BikeData
) {

    NavHost(
        navController = navController,
        startDestination = Constants.MAIN_SCREEN
    ) {
        composable(Constants.MAIN_SCREEN) {
            MainScreen(
                bikeData = bikeData,
                navigateToSettings = {
                    navController.navigate(Constants.SETTINGS_SCREEN)
                },
                navigateToStats = {
                    navController.navigate(Constants.STATISTICS_SCREEN)
                }
            )
        }
        composable(Constants.SETTINGS_SCREEN) {
            Text("Settings Screen")
        }
        composable(Constants.STATISTICS_SCREEN) {
            Text("Statistics screen")
        }
        }
}