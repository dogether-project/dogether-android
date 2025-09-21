package site.dogether.presentation.screen.home

import site.dogether.domain.model.group.Group
import site.dogether.domain.model.group.Group.Companion.STATUS_READY
import site.dogether.domain.model.todo.Todo
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.DialogState
import site.dogether.presentation.screen.home.model.Chip
import site.dogether.presentation.screen.home.state.TooltipUiState
import site.dogether.presentation.utils.DATE_FORMAT_SHORT_YEAR
import site.dogether.presentation.utils.toLocalDate
import site.dogether.presentation.utils.today
import java.time.LocalDate

data class HomeUiState(
    val isLoading: Boolean = false,
    val timerProgress: Float = 0f,
    val timerText: String = "",
    val selectedGroup: Group = Group(),
    val todoList: List<Todo> = emptyList(),
    val selectedDate: LocalDate = today,
    val selectedChip: Chip = Chip.All,
    val filteredTodoList: List<Todo> = listOf(),
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