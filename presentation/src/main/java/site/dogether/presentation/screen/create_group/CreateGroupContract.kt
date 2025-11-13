package site.dogether.presentation.screen.create_group

import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.dialog_state.DialogState

const val MinimumMemberCount = 2
const val MaximumMemberCount = 20

data class CreateGroupUiState(
    val isLoading: Boolean = false,
    val currentPage: Int = 0,
    val name: String = "",
    val maximumMemberCount: Int = 10,
    val duration: Int = 3,
    val isLaunchFromToday: Boolean = true,
    val duplicatedNameDialogState: DialogState = DialogState(false),
)

sealed interface CreateGroupUiEvent : UiEvent {
    sealed interface Typed : CreateGroupUiEvent {
        data class OnGroupNameTyped(val text: String) : Typed
    }

    sealed interface Click : CreateGroupUiEvent {
        data object OnClickBack : Click

        data object OnClickReduceMaximumMemberCount : Click

        data object OnClickAddMaximumMemberCount : Click

        data object OnClickNext : Click

        data class OnClickDuration(val duration: Int) : Click

        data class OnClickLaunchFrom(val isLaunchFromToday: Boolean) : Click

        data object OnClickCreateGroup : Click

        data object OnClickDuplicatedNameDialogNegative : Click

        data object OnClickDuplicatedNameDialogPositive : Click
    }

    sealed interface Callback : CreateGroupUiEvent {
        data object OnDuplicatedNameDialogDismissRequested : Callback
    }
}

sealed interface CreateGroupUiEffect : UiEffect {
    data object NavigateToBack : CreateGroupUiEffect

    data class NavigateToGroupCreated(val joinCode: String) : CreateGroupUiEffect
}