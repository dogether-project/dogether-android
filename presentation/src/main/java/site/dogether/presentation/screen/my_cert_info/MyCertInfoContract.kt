package site.dogether.presentation.screen.my_cert_info

import site.dogether.domain.model.todo.Todo
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class MyCertInfoUiState(
    val isLoading: Boolean = false,
    val todos: List<Todo> = listOf(),
    val selectedItemIndex: Int = 0,
    val title: String = "",
    val accessToken: String = "",
)

sealed interface MyCertInfoUiEvent : UiEvent {
    sealed interface Click : MyCertInfoUiEvent {
        data class OnClickItem(val index: Int) : Click

        data object OnClickCertificate : Click
    }

    sealed interface Callback : MyCertInfoUiEvent {
        data object OnSwipeLeft : Callback

        data object OnSwipeRight : Callback
    }
}

sealed interface MyCertInfoUiEffect : UiEffect {
    data class NavigateToCertificateTodo(
        val todoId: Long,
        val todoTitle: String,
    ) : MyCertInfoUiEffect
}