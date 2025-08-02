package site.dogether.presentation.screen.create_group

const val MinimumMemberLimit = 2
const val MaximumMemberLimit = 20

data class CreateGroupUiState(
    val isLoading: Boolean = false,
    val currentPage: Int = 0,
    val groupName: String = "",
    val memberLimit: Int = 10,
    val period: Int = 3,
    val isLaunchFromToday: Boolean = true
)

sealed interface CreateGroupUiEvent {
    sealed interface Type : CreateGroupUiEvent {
        data class OnGroupNameTyped(val text: String) : Type
    }

    sealed interface Click : CreateGroupUiEvent {
        data object OnClickBack : Click

        data object OnClickMinusMemberLimit : Click

        data object OnClickPlusMemberLimit : Click

        data object OnClickNext : Click

        data class OnClickPeriod(val period: Int) : Click

        data class OnClickLaunchFrom(val isLaunchFromToday: Boolean) : Click
    }
}

sealed interface CreateGroupUiEffect