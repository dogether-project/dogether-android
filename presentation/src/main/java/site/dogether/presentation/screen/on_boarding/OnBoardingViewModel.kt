package site.dogether.presentation.screen.on_boarding

import androidx.lifecycle.viewModelScope
import com.kakao.sdk.common.model.AuthError
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.user.CheckParticipatingUseCase
import site.dogether.domain.use_case.user.LoginWithKakaoUseCase
import site.dogether.domain.use_case.user.StoreUserInfoUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.on_boarding.OnBoardingUiEvent.Click.OnClickKakaoLogin

class OnBoardingViewModel(
    private val loginWithKakao: LoginWithKakaoUseCase,
    private val storeUserInfo: StoreUserInfoUseCase,
    private val checkParticipating: CheckParticipatingUseCase,
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
                        viewModelScope.launch {
                            val loginWithKakaoResult = loginWithKakao(
                                name = event.name,
                                idToken = event.idToken
                            ).getOrElse { e ->
                                // handle exception
                                return@launch
                            }

                            storeUserInfo(
                                name = loginWithKakaoResult.name,
                                accessToken = loginWithKakaoResult.accessToken
                            ).getOrElse { e ->
                                // handle exception
                                return@launch
                            }

                            val checkParticipatingResult = checkParticipating().getOrElse {
                                // handle exception
                                return@launch
                            }

                            if (checkParticipatingResult.shouldParticipating) {
                                postEffect(OnBoardingUiEffect.NavigateToParticipationMethod)
                            } else {
                                postEffect(OnBoardingUiEffect.NavigateToHome)
                            }
                        }
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

    companion object {
        const val KEY_KAKAO_URI = "key_kakao_uri"
    }
}