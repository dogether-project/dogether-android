package site.dogether.presentation.screen.todo.create

import site.dogether.domain.model.todo.Todo
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.dialog_state.DialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CreateTodoUiState(
    val todoItems: List<Todo> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val maxTodoCount: Int = 10,
    val addEnabled: Boolean = false,
    val ctaEnabled: Boolean = false,
    val todoText: String = "",
    val createTodoCheckDialogState: DialogState = DialogState(false),
) {
    val formattedDate: String = selectedDate.format(DateTimeFormatter.ofPattern("M월 d일 E요일"))
}

sealed interface CreateTodoUiEvent : UiEvent {
    sealed interface Lifecycle : CreateTodoUiEvent {
        data object OnStart : Lifecycle
    }

    data class UpdateTodoText(val text: String) : CreateTodoUiEvent
    data class RemoveTodoItem(val index: Int) : CreateTodoUiEvent
    data object AddTodoItem : CreateTodoUiEvent
    data object CreateTodos : CreateTodoUiEvent

    sealed interface CheckDialog : CreateTodoUiEvent {
        data object Confirm : CheckDialog
        data object Cancel : CheckDialog
    }
}

sealed interface CreateTodoSideEffect : UiEffect {
    data object Back : CreateTodoSideEffect
    data class ShowToast(val text: String) : CreateTodoSideEffect
}