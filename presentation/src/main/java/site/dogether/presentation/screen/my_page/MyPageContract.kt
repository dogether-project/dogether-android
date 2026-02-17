package site.dogether.presentation.screen.my_page

import androidx.compose.runtime.Immutable
import site.dogether.domain.model.user.UserInfo
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

@Immutable
data class MyPageUiState(
    val isLoading: Boolean = false,
    val userInfo: UserInfo? = UserInfo("", ""),
)

sealed interface MyPageUiEvent : UiEvent {
    data object OnStart : MyPageUiEvent

    sealed interface Click : MyPageUiEvent {
        data object OnClickStatistics : Click

        data object OnClickCertificationList : Click

        data object OnClickGroupManagement : Click

        data object OnClickSettings : Click
    }
}

sealed interface MyPageUiEffect : UiEffect {
    data object NavigateToStatistics : MyPageUiEffect

    data object NavigateToCertificationList : MyPageUiEffect

    data object NavigateToGroupManagement : MyPageUiEffect

    data object NavigateToSettings : MyPageUiEffect
}