package site.dogether.presentation.screen.home

import site.dogether.presentation.R
import site.dogether.presentation.model.Todo
import site.dogether.presentation.model.Todo.Companion.STATUS_APPROVE
import site.dogether.presentation.model.Todo.Companion.STATUS_CERTIFY_PENDING
import site.dogether.presentation.model.Todo.Companion.STATUS_REJECT
import site.dogether.presentation.model.Todo.Companion.STATUS_REVIEW_PENDING
import site.dogether.presentation.screen.home.model.Chip
import site.dogether.presentation.screen.home.state.TooltipUiState

data class HomeUiState(
    val isLoading: Boolean = false,
    val timerProgress: Float = 0f,
    val timerText: String = "",
    val currentGroup: String = "DND 작심삼일 탈출러",
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
    val filteredTodoList: List<Todo> = listOf(),
    val isChooseGroupBottomSheetExpanded: Boolean = false,
    val groupList: List<String> = listOf("DND 작심삼일 탈출러", "배고픈 민족들"),
    val tooltipUiState: TooltipUiState = TooltipUiState(
        isShowing = true,
        stringId = R.string.tooltip_group_finished
    ),
)

sealed interface HomeUiEvent {
    sealed interface Click : HomeUiEvent {
        data class OnClickChip(val chip: Chip) : Click

        data object OnClickChooseGroup : Click
    }

    sealed interface Callback : HomeUiEvent {
        data object OnChooseGroupBottomSheetDismissRequested : Callback
    }
}

sealed interface HomeUiEffect {

}