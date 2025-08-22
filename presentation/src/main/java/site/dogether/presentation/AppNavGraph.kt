package site.dogether.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import site.dogether.presentation.screen.create_group.CreateGroupScreen
import site.dogether.presentation.screen.error.ErrorScreen
import site.dogether.presentation.screen.force_update.ForceUpdateScreen
import site.dogether.presentation.screen.group_created.GroupCreatedScreen
import site.dogether.presentation.screen.group_participated.GroupParticipatedScreen
import site.dogether.presentation.screen.home.HomeScreen
import site.dogether.presentation.screen.my_page.MyPageScreen
import site.dogether.presentation.screen.my_page.screen.certification_list.CertificationListScreen
import site.dogether.presentation.screen.my_page.screen.group_management.GroupManagementScreen
import site.dogether.presentation.screen.my_page.screen.settings.SettingsScreen
import site.dogether.presentation.screen.my_page.screen.statistics.StatisticsScreen
import site.dogether.presentation.screen.on_boarding.OnBoardingScreen
import site.dogether.presentation.screen.participate_group.ParticipateGroupScreen
import site.dogether.presentation.screen.participation_method.ParticipationMethodScreen
import site.dogether.presentation.screen.splash.SplashScreen
import site.dogether.presentation.utils.LocalNavHostController

@Composable
fun AppNavGraph(startDestination: String = Screen.SPLASH) {
    val navHostController = LocalNavHostController.current

    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(Screen.SPLASH) { SplashScreen() }
        composable(Screen.FORCE_UPDATE) { ForceUpdateScreen() }
        composable(Screen.ON_BOARDING) { OnBoardingScreen() }
        composable(Screen.PARTICIPATION_METHOD) { ParticipationMethodScreen() }
        composable(Screen.CREATE_GROUP) { CreateGroupScreen() }
        composable(Screen.GROUP_CREATED) { GroupCreatedScreen() }
        composable(Screen.PARTICIPATE_GROUP) { ParticipateGroupScreen() }
        composable(Screen.GROUP_PARTICIPATED) { GroupParticipatedScreen() }
        composable(Screen.ERROR) { ErrorScreen() }
        composable(Screen.HOME) { HomeScreen() }
        composable(Screen.MY_PAGE) { MyPageScreen() }
        composable(Screen.STATISTICS) { StatisticsScreen() }
        composable(Screen.CERTIFICATION_LIST) { CertificationListScreen() }
        composable(Screen.SETTINGS) { SettingsScreen() }
        composable(Screen.GROUP_MANAGEMENT) { GroupManagementScreen() }
    }
}