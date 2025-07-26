package site.dogether.presentation.screen.splash

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SplashViewModel :
    ContainerHost<SplashUiState, SplashUiEffect>,
    ViewModel() {

    override val container: Container<SplashUiState, SplashUiEffect> = container(SplashUiState())
}