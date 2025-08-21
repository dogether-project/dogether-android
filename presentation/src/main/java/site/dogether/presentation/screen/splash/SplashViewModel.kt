package site.dogether.presentation.screen.splash

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import site.dogether.domain.use_case.app_info.CheckUpdateRequiredUseCase
import site.dogether.presentation.base.BaseViewModel

class SplashViewModel(
    private val checkUpdateRequiredUseCase: CheckUpdateRequiredUseCase,
) : BaseViewModel<SplashUiState, SplashUiEvent, SplashUiEffect>(SplashUiState()) {

    override val container: Container<SplashUiState, SplashUiEffect> = container(SplashUiState())

    override fun onEvent(event: SplashUiEvent) {
        when (event) {
            is SplashUiEvent.Lifecycle -> {
                when (event) {
                    is SplashUiEvent.Lifecycle.OnStart -> {
                        postEffect(SplashUiEffect.GetAppVersion)
                    }
                }
            }

            is SplashUiEvent.Callback -> {
                when (event) {
                    is SplashUiEvent.Callback.OnGetAppVersion -> {
                        viewModelScope.launch {
                            checkUpdateRequiredUseCase(event.appVersion).onSuccess {

                            }.onFailure {

                            }
                        }
                    }
                }
            }
        }
    }
}