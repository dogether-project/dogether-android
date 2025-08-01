package site.dogether.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import site.dogether.presentation.screen.force_update.ForceUpdateScreen
import site.dogether.presentation.screen.on_boarding.OnBoardingScreen
import site.dogether.presentation.screen.participation_method.ParticipationMethodScreen
import site.dogether.presentation.screen.splash.SplashScreen

@Composable
fun AppNavGraph(
    startDestination: String = Screen.Splash.route,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) { SplashScreen() }
        composable(Screen.ForceUpdate.route) { ForceUpdateScreen() }
        composable(Screen.OnBoarding.route) { OnBoardingScreen() }
        composable(Screen.ParticipationMethod.route) { ParticipationMethodScreen() }
    }
}

enum class Screen(val route: String) {
    Splash("splash"),
    ForceUpdate("force_update"),
    OnBoarding("on_boarding"),
    ParticipationMethod("participation_method")
}