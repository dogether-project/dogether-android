package site.dogether.presentation.screen.home

import site.dogether.domain.model.group.Group
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.DialogState
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
    val selectedGroup: Group = Group(),
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
    val isSelectGroupBottomSheetShowing: Boolean = false,
    val permissionDialogState: DialogState = DialogState(),
    val groups: List<Group> = listOf(),
    val tooltipUiState: TooltipUiState = TooltipUiState(
        isShowing = true,
        stringId = R.string.tooltip_group_finished
    ),
)

sealed interface HomeUiEvent : UiEvent {
    sealed interface Lifecycle : HomeUiEvent {
        data object OnFirstComposition : Lifecycle
    }

    sealed interface Click : HomeUiEvent {
        data class OnClickChip(val chip: Chip) : Click

        data object OnClickSelectGroup : Click

        data object OnClickPermissionDialogNegative : Click

        data object OnClickPermissionDialogPositive : Click

        data object OnClickAddGroup : Click
    }

    sealed interface Callback : HomeUiEvent {
        data object OnPermissionDialogDismissRequested : Callback

        data object OnSelectGroupBottomSheetDismissRequested : Callback

        data object OnNotificationPermissionDenied : Callback
    }
}

sealed interface HomeUiEffect : UiEffect {
    data object CheckNotificationPermission : HomeUiEffect

    data object NavigateToNotificationSettings : HomeUiEffect
}