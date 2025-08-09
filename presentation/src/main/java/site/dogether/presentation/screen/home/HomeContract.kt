package site.dogether.presentation.screen.home

import site.dogether.presentation.model.Todo
import site.dogether.presentation.model.Todo.Companion.STATUS_APPROVE
import site.dogether.presentation.model.Todo.Companion.STATUS_CERTIFY_PENDING
import site.dogether.presentation.model.Todo.Companion.STATUS_REJECT
import site.dogether.presentation.model.Todo.Companion.STATUS_REVIEW_PENDING
import site.dogether.presentation.screen.home.state.Chip

data class HomeUiState(
    val isLoading: Boolean = false,
    val timerProgress: Float = 0f,
    val timerText: String = "",
    val todoList: List<Todo> = listOf(
        Todo(
            id = 1,
            content = "테스트 컨텐츠",
            status = STATUS_CERTIFY_PENDING,
        ),
        Todo(
            id = 2,
            content = "테스트 컨텐츠",
            status = STATUS_APPROVE,
        ),
        Todo(
            id = 3,
            content = "테스트 컨텐츠",
            status = STATUS_REJECT,
        ),
        Todo(
            id = 4,
            content = "테스트 컨텐츠",
            status = STATUS_REVIEW_PENDING,
        ),
    ),
    val selectedChip: Chip = Chip.All,
    val filteredTodoList: List<Todo> = listOf()
)

sealed interface HomeUiEvent {
    sealed interface Click : HomeUiEvent {
        data class OnClickChip(val chip: Chip) : Click
    }
}

sealed interface HomeUiEffect {

}