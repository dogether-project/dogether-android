package site.dogether.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import site.dogether.KEY_ENCODED_URI
import site.dogether.KEY_GROUP_ID
import site.dogether.KEY_JOIN_CODE
import site.dogether.KEY_SELECTED_DATE
import site.dogether.KEY_TODO_ID
import site.dogether.KEY_TODO_INDEX
import site.dogether.KEY_TODO_TITLE
import site.dogether.presentation.screen.create_group.CreateGroupScreen
import site.dogether.presentation.screen.error.ErrorScreen
import site.dogether.presentation.screen.force_update.ForceUpdateScreen
import site.dogether.presentation.screen.group_created.GroupCreatedScreen
import site.dogether.presentation.screen.group_participated.GroupParticipatedScreen
import site.dogether.presentation.screen.home.HomeScreen
import site.dogether.presentation.screen.my_cert_info.MyCertInfoScreen
import site.dogether.presentation.screen.my_page.MyPageScreen
import site.dogether.presentation.screen.my_page.screen.certification_list.CertificationListScreen
import site.dogether.presentation.screen.my_page.screen.group_management.GroupManagementScreen
import site.dogether.presentation.screen.my_page.screen.settings.SettingsScreen
import site.dogether.presentation.screen.my_page.screen.statistics.StatisticsScreen
import site.dogether.presentation.screen.on_boarding.OnBoardingScreen
import site.dogether.presentation.screen.participate_group.ParticipateGroupScreen
import site.dogether.presentation.screen.participation_method.ParticipationMethodScreen
import site.dogether.presentation.screen.splash.SplashScreen
import site.dogether.presentation.screen.todo.certificate.CertificateDescriptionScreen
import site.dogether.presentation.screen.todo.certificate.CertificateTodoScreen
import site.dogether.presentation.screen.todo.create.CreateTodoScreen
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
        composable(
            route = "${Screen.GROUP_CREATED}/{${KEY_JOIN_CODE}}",
            arguments = listOf(navArgument(KEY_JOIN_CODE) { type = NavType.StringType }
            )
        ) { GroupCreatedScreen() }
        composable(Screen.PARTICIPATE_GROUP) { ParticipateGroupScreen() }
        composable(Screen.GROUP_PARTICIPATED) { GroupParticipatedScreen() }
        composable(Screen.ERROR) { ErrorScreen() }
        composable(Screen.HOME) { HomeScreen() }
        composable(Screen.MY_PAGE) { MyPageScreen() }
        composable(Screen.STATISTICS) { StatisticsScreen() }
        composable(Screen.CERTIFICATION_LIST) { CertificationListScreen() }
        composable(Screen.SETTINGS) { SettingsScreen() }
        composable(Screen.GROUP_MANAGEMENT) { GroupManagementScreen() }
        composable(
            route = "${Screen.MY_CERT_INFO}/{${KEY_GROUP_ID}}/{${KEY_TODO_INDEX}}",
            arguments = listOf(
                navArgument(KEY_GROUP_ID) { type = NavType.IntType },
                navArgument(KEY_TODO_INDEX) { type = NavType.IntType }
            )
        ) { MyCertInfoScreen() }
        composable(
            route = "${Screen.CREATE_TODO}/{${KEY_GROUP_ID}}/{${KEY_SELECTED_DATE}}",
            arguments = listOf(
                navArgument(KEY_GROUP_ID) { type = NavType.IntType },
                navArgument(KEY_SELECTED_DATE) { type = NavType.StringType }
            )
        ) { CreateTodoScreen() }
        composable(
            route = "${Screen.CERTIFICATE_TODO}/{${KEY_TODO_ID}}/{${KEY_TODO_TITLE}}",
            arguments = listOf(
                navArgument(KEY_TODO_ID) { type = NavType.IntType },
                navArgument(KEY_TODO_TITLE) { type = NavType.StringType }
            )
        ) { CertificateTodoScreen() }
        composable(
            route = "${Screen.CERTIFICATE_DESCRIPTION}/{${KEY_TODO_ID}}/{${KEY_ENCODED_URI}}",
            arguments = listOf(
                navArgument(KEY_TODO_ID) { type = NavType.IntType },
                navArgument(KEY_ENCODED_URI) { type = NavType.StringType }
            )
        ) {
            CertificateDescriptionScreen()
        }
    }
}