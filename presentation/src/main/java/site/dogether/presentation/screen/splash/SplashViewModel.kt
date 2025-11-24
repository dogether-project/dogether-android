package site.dogether.presentation.screen.splash

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import site.dogether.domain.use_case.app_info.CheckUpdateRequiredUseCase
import site.dogether.domain.use_case.user.CheckParticipatingUseCase
import site.dogether.domain.use_case.user.GetUserInfoUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

class SplashViewModel(
    private val checkUpdateRequired: CheckUpdateRequiredUseCase,
    private val getUserInfo: GetUserInfoUseCase,
    private val checkParticipating: CheckParticipatingUseCase,
) : BaseViewModel<SplashUiState>(SplashUiState()) {

    override val container: Container<SplashUiState, UiEffect> = container(SplashUiState())

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

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
                            val checkUpdateRequiredResult = checkUpdateRequired(event.appVersion).getOrElse { e ->
                                // handle exception
                                return@launch
                            }

                            if (checkUpdateRequiredResult.isForceUpdateRequired) {
                                // force update
                                return@launch
                            }

                            val userInfo = getUserInfo().getOrElse { e ->
                                // handle exception
                                return@launch
                            }

                            if (userInfo.accessToken.isEmpty()) {
                                postEffect(SplashUiEffect.NavigateToOnBoarding)
                                return@launch
                            }

                            val checkParticipatingResult = checkParticipating().getOrElse {
                                // handle exception
                                postEffect(SplashUiEffect.NavigateToOnBoarding)
                                return@launch
                            }

                            if (checkParticipatingResult.shouldParticipating) {
                                postEffect(SplashUiEffect.NavigateToParticipationMethod)
                            } else {
                                postEffect(SplashUiEffect.NavigateToHome)
                            }
                        }
                    }
                }
            }
        }
    }
}