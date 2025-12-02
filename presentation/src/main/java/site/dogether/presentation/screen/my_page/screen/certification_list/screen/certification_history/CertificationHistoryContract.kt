package site.dogether.presentation.screen.my_page.screen.certification_list.screen.certification_history

import site.dogether.domain.model.todo.Todo
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class CertificationHistoryUiState(
    val isLoading: Boolean = false,
    val todos: List<Todo> = listOf(),
    val selectedItemIndex: Int = 0,
    val title: String = "",
)

sealed interface CertificationHistoryUiEvent : UiEvent {
    sealed interface Click : CertificationHistoryUiEvent {
        data class OnClickItem(val index: Int) : Click

        data object OnClickCertificate : Click
    }

    sealed interface Callback : CertificationHistoryUiEvent {
        data object OnSwipeLeft : Callback

        data object OnSwipeRight : Callback
    }
}

sealed interface CertificationHistoryUiEffect : UiEffect {

}