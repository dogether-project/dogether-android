package site.dogether.presentation.screen.my_page

import site.dogether.domain.model.user.UserInfo

data class MyPageUiState(
    val isLoading: Boolean = false,
    val userInfo: UserInfo? = UserInfo("지호", ""),
)

sealed interface MyPageUiEvent {

}

sealed interface MyPageUiEffect {

}