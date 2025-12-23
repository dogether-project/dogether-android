package site.dogether.presentation.screen.certificate.member_cert_info

import site.dogether.domain.model.todo.Todo
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class MemberCertInfoUiState(
    val isLoading: Boolean = false,
    val todos: List<Todo> = listOf(),
    val selectedItemIndex: Int = 0,
    val name: String = "",
)

sealed interface MemberCertInfoUiEvent : UiEvent {
    sealed interface Click : MemberCertInfoUiEvent {
        data class OnClickItem(val index: Int) : Click
    }

    sealed interface Callback : MemberCertInfoUiEvent {
        data object OnSwipeLeft : Callback

        data object OnSwipeRight : Callback
    }
}

sealed interface MemberCertInfoUiEffect : UiEffect

