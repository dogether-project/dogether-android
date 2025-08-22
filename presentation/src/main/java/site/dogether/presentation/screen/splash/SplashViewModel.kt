package site.dogether.presentation.screen.splash

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import site.dogether.domain.use_case.app_info.CheckUpdateRequiredUseCase
import site.dogether.domain.use_case.user.GetUserTokenUseCase
import site.dogether.presentation.base.BaseViewModel

class SplashViewModel(
    private val checkUpdateRequiredUseCase: CheckUpdateRequiredUseCase,
    private val getUserTokenUseCase: GetUserTokenUseCase,
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
                            val checkUpdateRequiredResult = checkUpdateRequiredUseCase(event.appVersion).getOrElse { e ->
                                // handle exception
                                return@launch
                            }

                            if (checkUpdateRequiredResult.isForceUpdateRequired) {
                                // force update
                                return@launch
                            }

                            val userToken = getUserTokenUseCase().getOrElse { e ->
                                // handle exception
                                return@launch
                            }

                            if (userToken.isEmpty()) {
                                postEffect(SplashUiEffect.NavigateToOnBoarding)
                                return@launch
                            }

//                            val isParticipatedGroupExist
                        }
                    }
                }
            }
        }
    }
}