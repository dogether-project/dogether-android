package site.dogether.presentation.screen.splash

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SplashViewModel :
    ContainerHost<SplashUiState, SplashUiEffect>,
    ViewModel() {

    override val container: Container<SplashUiState, SplashUiEffect> = container(SplashUiState())

    fun onStarted() = intent {
        postSideEffect(SplashUiEffect.CheckNotificationPermission)
    }

    fun onPermissionGranted() = intent {

    }

    fun onPermissionDenied() = intent {
        reduce {
            state.copy(isPermissionDialogShowing = true)
        }
    }

    fun onPermissionDialogDismissRequested() = intent {
        reduce {
            state.copy(isPermissionDialogShowing = false)
        }
    }

    fun onClickNavigateToNotificationSetting() = intent {
        reduce {
            state.copy(isPermissionDialogShowing = false)
        }

        postSideEffect(SplashUiEffect.NavigateToNotificationSetting)
    }
}