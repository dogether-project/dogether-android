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
import site.dogether.presentation.base.UiEvent
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
                        viewModelScope.launch {
                            val loginWithKakaoResult = loginWithKakao(
                                name = event.name,
                                idToken = event.idToken
                            ).getOrElse {
                                // handle exception
                                return@launch
                            }

                            storeUserInfo(
                                name = loginWithKakaoResult.name,
                                accessToken = loginWithKakaoResult.accessToken
                            ).getOrElse {
                                // handle exception
                                return@launch
                            }

                            val checkParticipatingResult = checkParticipating().getOrElse {
                                // handle exception
                                return@launch
                            }

                            // 딥링크로 받은 코드가 있으면 ParticipateGroupScreen으로 이동
                            val groupJoinCode = getGroupJoinCodeUseCase()
                            if (groupJoinCode.isNotEmpty()) {
                                // 저장된 딥링크 정보 삭제
                                postEffect(
                                    OnBoardingUiEffect.NavigateToParticipateGroup(
                                        groupJoinCode
                                    )
                                )
                                storeGroupJoinCodeUseCase()
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