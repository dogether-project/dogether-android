package site.dogether.presentation.screen.create_group

import site.dogether.presentation.model.DialogState

const val MinimumMemberLimit = 2
const val MaximumMemberLimit = 20

data class CreateGroupUiState(
    val isLoading: Boolean = false,
    val currentPage: Int = 0,
    val name: String = "",
    val memberLimit: Int = 10,
    val duration: Int = 3,
    val isLaunchFromToday: Boolean = true,
    val duplicatedNameDialogState: DialogState = DialogState(false),
)

sealed interface CreateGroupUiEvent {
    sealed interface Typed : CreateGroupUiEvent {
        data class OnGroupNameTyped(val text: String) : Typed
    }

    sealed interface Click : CreateGroupUiEvent {
        data object OnClickBack : Click

        data object OnClickMinusMemberLimit : Click

        data object OnClickPlusMemberLimit : Click

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

sealed interface CreateGroupUiEffect {
    data object NavigateToBack : CreateGroupUiEffect
}