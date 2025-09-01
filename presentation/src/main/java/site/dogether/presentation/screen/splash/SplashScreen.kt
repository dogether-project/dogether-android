package site.dogether.presentation.screen.splash

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.Screen
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.LifecycleEvent
import site.dogether.presentation.utils.LocalNavHostController
import site.dogether.presentation.utils.ScreenPreview

@Composable
fun SplashScreen(viewModel: SplashViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value
    val onEvent: (UiEvent) -> Unit = { uiEvent -> viewModel.onEvent(uiEvent) }
    val context = LocalContext.current
    val navHostController = LocalNavHostController.current

    viewModel.CollectEffect<SplashUiEffect> { uiEffect ->
        when (uiEffect) {
            is SplashUiEffect.GetAppVersion -> {
                getAppVersion(
                    context = context,
                    onSuccess = { appVersion -> onEvent(SplashUiEvent.Callback.OnGetAppVersion(appVersion)) },
                    onFailure = {}
                )
            }

            is SplashUiEffect.NavigateToOnBoarding -> navigateToOnBoarding(navHostController)

            is SplashUiEffect.NavigateToHome -> navigateToHome(navHostController)

            is SplashUiEffect.NavigateToParticipationMethod -> navigateToParticipationMethod(navHostController)
        }
    }

    LifecycleEvent(Lifecycle.Event.ON_START) {
        onEvent(SplashUiEvent.Lifecycle.OnStart)
    }

    SplashScreenContents()
}

private fun getAppVersion(
    context: Context,
    onSuccess: (String) -> Unit,
    onFailure: () -> Unit,
) {
    context.packageManager.getPackageInfo(context.packageName, 0).versionName?.let { appVersion ->
        onSuccess(appVersion)
    } ?: onFailure()
}

private fun navigateToOnBoarding(navHostController: NavHostController) {
    navHostController.navigate(Screen.ON_BOARDING)
}

private fun navigateToHome(navHostController: NavHostController) {
    navHostController.navigate(Screen.HOME)
}

private fun navigateToParticipationMethod(navHostController: NavHostController) {
    navHostController.navigate(Screen.PARTICIPATION_METHOD)
}

@Composable
private fun SplashScreenContents() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(R.drawable.ic_splash),
            contentDescription = "icon_splash"
        )
    }
}

@ScreenPreview
@Composable
private fun SplashScreenContentsPreview() {
    SplashScreenContents()
}