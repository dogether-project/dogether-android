package site.dogether.presentation.screen.home

import site.dogether.common.utils.DateTimeUtils.DATE_FORMAT_SHORT_YEAR
import site.dogether.common.utils.DateTimeUtils.toLocalDate
import site.dogether.common.utils.DateTimeUtils.today
import site.dogether.domain.model.group.Group
import site.dogether.domain.model.group.Group.Companion.STATUS_READY
import site.dogether.domain.model.todo.Todo
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.dialog_state.DialogState
import site.dogether.presentation.screen.home.model.Chip
import site.dogether.presentation.screen.home.state.TooltipUiState
import java.time.LocalDate

data class HomeUiState(
    val isLoading: Boolean = false,
    val timerProgress: Float = 0f,
    val timerText: String = "",
    val selectedGroup: Group = Group(),
    val todoList: List<Todo> = emptyList(),
    val selectedDate: LocalDate = today,
    val selectedChip: Chip = Chip.All,
    val isSelectGroupBottomSheetShowing: Boolean = false,
    val permissionDialogState: DialogState = DialogState(),
    val groups: List<Group> = listOf(),
    val tooltipUiState: TooltipUiState = TooltipUiState(),
) {
    val isGoPrevDayPossible: Boolean
        get() = if (selectedGroup.startAt.isNotEmpty()) {
            when (selectedGroup.status) {
                STATUS_READY -> false
                else -> selectedDate != selectedGroup.startAt.toLocalDate(DATE_FORMAT_SHORT_YEAR)
            }
        } else false
    val isGoNextDayPossible: Boolean
        get() = selectedDate < today
    val filteredTodoList: List<Todo>
        get() = when (selectedChip) {
            Chip.All -> todoList
            Chip.Approve -> todoList.filter { it.status == Todo.STATUS_APPROVE }
            Chip.Reject -> todoList.filter { it.status == Todo.STATUS_REJECT }
            Chip.ReviewPending -> todoList.filter { it.status == Todo.STATUS_REVIEW_PENDING }
        }
}

sealed interface HomeUiEvent : UiEvent {
    sealed interface Lifecycle : HomeUiEvent {
        data object OnFirstComposition : Lifecycle
    }

    sealed interface Click : HomeUiEvent {
        data class OnClickChip(val chip: Chip) : Click

        data object OnClickSelectGroup : Click

        data object OnClickPermissionDialogNegative : Click

        data object OnClickPermissionDialogPositive : Click

        data class OnClickGroup(val group: Group) : Click

        data object OnClickAddGroup : Click

        data object OnClickPrevDay : Click

        data object OnClickNextDay : Click

        data object OnClickDismissTooltip : Click

        data object OnClickCreateTodo : Click

        data class OnClickCertificateTodo(val todoId: Long, val todoTitle: String) : Click

        data class OnClickTodo(val todoIndex: Int) : Click

        data object OnClickMyPage : Click

        data object OnClickRanking : Click
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

    data object NavigateToCreateTodo : HomeUiEffect

    data class NavigateToCertificateTodo(val todoId: Long, val todoTitle: String) : HomeUiEffect

    data class NavigateToMyCertInfo(
        val groupId: Int, val todoIndex: Int, val date: String
    ) : HomeUiEffect

    data object NavigateToMyPage : HomeUiEffect

    data class NavigateToRanking(val groupId: Int) : HomeUiEffect
}