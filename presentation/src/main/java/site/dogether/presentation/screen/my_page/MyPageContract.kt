package site.dogether.presentation.screen.my_page

import site.dogether.domain.model.user.UserInfo
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class MyPageUiState(
    val isLoading: Boolean = false,
    val userInfo: UserInfo? = UserInfo("지호", ""),
)

sealed interface MyPageUiEvent : UiEvent {

}

sealed interface MyPageUiEffect : UiEffect {

}