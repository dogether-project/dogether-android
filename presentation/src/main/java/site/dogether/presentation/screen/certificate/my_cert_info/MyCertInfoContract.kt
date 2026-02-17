package site.dogether.presentation.screen.certificate.my_cert_info

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import site.dogether.domain.model.todo.Todo
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

@Immutable
data class MyCertInfoUiState(
    val isLoading: Boolean = false,
    val todos: ImmutableList<Todo> = persistentListOf(),
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