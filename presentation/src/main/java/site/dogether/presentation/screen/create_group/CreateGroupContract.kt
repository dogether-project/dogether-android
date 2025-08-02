package site.dogether.presentation.screen.create_group

const val MinimumMemberLimit = 2
const val MaximumMemberLimit = 20

data class CreateGroupUiState(
    val isLoading: Boolean = false,
    val currentPage: Int = 0,
    val groupName: String = "",
    val memberLimit: Int = 10
)

sealed interface CreateGroupUiEvent {
    sealed interface Type : CreateGroupUiEvent {
        data class OnGroupNameTyped(val text: String) : Type
    }

    sealed interface Click : CreateGroupUiEvent {
        data object OnClickMinusMemberLimit : Click

        data object OnClickPlusMemberLimit : Click

        data object OnClickNext : Click
    }
}

sealed interface CreateGroupUiEffect