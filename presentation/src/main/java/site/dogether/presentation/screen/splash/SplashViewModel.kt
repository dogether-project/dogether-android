package site.dogether.presentation.screen.splash

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import site.dogether.common.utils.DeeplinkConstants
import site.dogether.domain.use_case.app_info.CheckUpdateRequiredUseCase
import site.dogether.domain.use_case.todo.GetPendingReviewCertificationsUseCase
import site.dogether.domain.use_case.user.CheckParticipatingUseCase
import site.dogether.domain.use_case.user.GetUserInfoUseCase
import site.dogether.domain.use_case.user.StoreGroupJoinCodeUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error

class SplashViewModel(
    private val checkUpdateRequired: CheckUpdateRequiredUseCase,
    private val getUserInfo: GetUserInfoUseCase,
    private val checkParticipating: CheckParticipatingUseCase,
    private val storeGroupJoinCodeUseCase: StoreGroupJoinCodeUseCase,
    private val getPendingReviewCertifications: GetPendingReviewCertificationsUseCase,
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
                        checkVersionAndNavigate(event.appVersion)
                    }
                }
            }

            is SplashUiEvent.Deeplink -> {
                when (event) {
                    is SplashUiEvent.Deeplink.OnDeeplinkReceived -> {
                        updateState {
                            it.copy(deeplink = event.link.orEmpty())
                        }
                    }
                }
            }

            is SplashUiEvent.ShowToast -> {
                showToast(event.text)
            }
        }
    }

    private fun checkVersionAndNavigate(appVersion: String) {
        viewModelScope.launch {
            val checkUpdateRequiredResult =
                checkUpdateRequired(appVersion).getOrElse {
                    handleError(error = Error.LoadData, appVersion = appVersion)
                    return@launch
                }

            if (checkUpdateRequiredResult.isForceUpdateRequired) {
                handleError(error = Error.LoadData, appVersion = appVersion)
                return@launch
            }

            delay(800L)
            // 딥링크에서 joinCode 추출
            val joinCode = runCatching {
                uiState.deeplink?.toUri()?.getQueryParameter(DeeplinkConstants.QUERY_CODE)
            }.getOrNull()

            val userInfo = getUserInfo().getOrElse {
                handleError(error = Error.LoadData, appVersion = appVersion)
                return@launch
            }

            if (userInfo.accessToken.isEmpty()) {
                // 토큰 없을 때는 딥링크 정보를 저장하고 온보딩으로 이동
                // 로그인 이후에 그룹 가입 절차로 이동시에 여기서 받은 코드 입력하도록 처리
                joinCode?.let { storeGroupJoinCodeUseCase(it) }
                postEffect(SplashUiEffect.NavigateToOnBoarding)
                return@launch
            }

            // 리뷰 대기가 있는 경우
            val getPendingReviewCertifications =
                getPendingReviewCertifications().getOrElse {
                    handleError(error = Error.LoadData, appVersion = appVersion)
                    return@launch
                }

            if (getPendingReviewCertifications.certifications.isNotEmpty()) {
                postEffect(SplashUiEffect.NavigateToReviewCertification)
                return@launch
            }

            val checkParticipatingResult = checkParticipating().getOrElse {
                postEffect(SplashUiEffect.NavigateToOnBoarding)
                return@launch
            }

            if (checkParticipatingResult.shouldParticipating) {
                // 그룹 참여가 필요한 경우
                if (joinCode != null) {
                    // 딥링크로 받은 코드를 이용해서 코드 입력 페이지로 이동
                    postEffect(SplashUiEffect.NavigateToParticipateGroup(joinCode))
                } else {
                    postEffect(SplashUiEffect.NavigateToParticipationMethod)
                }
                return@launch
            } else {
                // 이미 그룹에 참여 중인 경우
                if (joinCode != null) {
                    // 딥링크로 받은 코드를 이용해서 코드 입력 페이지로 이동
                    postEffect(SplashUiEffect.NavigateToParticipateGroup(joinCode))
                } else {
                    postEffect(SplashUiEffect.NavigateToHome)
                }
                return@launch
            }
        }
    }

    private fun handleError(error: Error, appVersion: String) {
        postEffect(
            UiEffect.NavigateToErrorWithCallback(
                error = error,
                onPositive = { checkVersionAndNavigate(appVersion) }
            )
        )
    }
}
