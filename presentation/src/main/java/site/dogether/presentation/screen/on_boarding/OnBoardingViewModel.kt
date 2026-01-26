package site.dogether.presentation.screen.on_boarding

import androidx.lifecycle.viewModelScope
import com.kakao.sdk.common.model.AuthError
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.user.CheckParticipatingUseCase
import site.dogether.domain.use_case.user.GetGroupJoinCodeUseCase
import site.dogether.domain.use_case.user.LoginWithKakaoUseCase
import site.dogether.domain.use_case.user.StoreGroupJoinCodeUseCase
import site.dogether.domain.use_case.user.StoreUserInfoUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error
import site.dogether.presentation.screen.on_boarding.OnBoardingUiEvent.Click.OnClickKakaoLogin

class OnBoardingViewModel(
    private val loginWithKakao: LoginWithKakaoUseCase,
    private val storeUserInfo: StoreUserInfoUseCase,
    private val checkParticipating: CheckParticipatingUseCase,
    private val getGroupJoinCodeUseCase: GetGroupJoinCodeUseCase,
    private val storeGroupJoinCodeUseCase: StoreGroupJoinCodeUseCase
) : BaseViewModel<OnBoardingUiState>(OnBoardingUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is OnBoardingUiEvent.Click -> {
                when (event) {
                    is OnClickKakaoLogin -> {
                        postEffect(OnBoardingUiEffect.CheckLoginWithKakaoTalkPossibility)
                    }
                }
            }

            is OnBoardingUiEvent.Callback -> {
                when (event) {
                    is OnBoardingUiEvent.Callback.OnLoginWithKakaoTalkPossible -> {
                        viewModelScope.launch {
                            postEffect(if (event.isPossible) OnBoardingUiEffect.LoginWithKakaoTalk else OnBoardingUiEffect.LoginWithKakaoAccount)
                        }
                    }

                    is OnBoardingUiEvent.Callback.OnSuccessKakaoLogin -> {
                        handleKakaoLoginSuccess(event.name, event.idToken)
                    }

                    is OnBoardingUiEvent.Callback.OnErrorKakaoLogin -> {
                        viewModelScope.launch {
                            when ((event.throwable as? AuthError)?.statusCode) {
                                302 -> postEffect(OnBoardingUiEffect.LoginWithKakaoAccount)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleKakaoLoginSuccess(name: String, idToken: String) {
        viewModelScope.launch {
            val loginWithKakaoResult = loginWithKakao(
                name = name,
                idToken = idToken
            ).getOrElse {
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { handleKakaoLoginSuccess(name, idToken) }
                    )
                )
                return@launch
            }

            storeUserInfo(
                name = loginWithKakaoResult.name,
                accessToken = loginWithKakaoResult.accessToken
            ).getOrElse {
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { handleKakaoLoginSuccess(name, idToken) }
                    )
                )
                return@launch
            }

            val checkParticipatingResult = checkParticipating().getOrElse {
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { handleKakaoLoginSuccess(name, idToken) }
                    )
                )
                return@launch
            }

            // 딥링크로 받은 코드가 있으면 ParticipateGroupScreen으로 이동
            val groupJoinCode = getGroupJoinCodeUseCase()
            if (groupJoinCode.isNotEmpty()) {
                // 저장된 딥링크 정보 삭제
                storeGroupJoinCodeUseCase()

                postEffect(
                    OnBoardingUiEffect.NavigateToParticipateGroup(
                        groupJoinCode
                    )
                )
            } else {
                // 딥링크가 없으면 기존 로직대로 진행
                if (checkParticipatingResult.shouldParticipating) {
                    postEffect(OnBoardingUiEffect.NavigateToParticipationMethod)
                } else {
                    postEffect(OnBoardingUiEffect.NavigateToHome)
                }
            }
        }
    }

    companion object {
        const val KEY_KAKAO_URI = "key_kakao_uri"
    }
}