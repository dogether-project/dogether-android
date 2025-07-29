package site.dogether.presentation.screen.splash

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import site.dogether.presentation.base.BaseViewModel

class SplashViewModel : BaseViewModel<SplashUiState, SplashUiEvent, SplashUiEffect>(SplashUiState()) {

    override val container: Container<SplashUiState, SplashUiEffect> = container(SplashUiState())

    override fun onEvent(event: SplashUiEvent) {
        when (event) {
            is SplashUiEvent.Lifecycle -> {
                when(event) {
                    is SplashUiEvent.Lifecycle.OnStart -> {
                        viewModelScope.launch {

                        }
                    }
                }
            }
        }
    }
}