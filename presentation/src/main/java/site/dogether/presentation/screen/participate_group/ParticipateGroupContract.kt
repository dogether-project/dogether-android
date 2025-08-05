package site.dogether.presentation.screen.participate_group

data class ParticipateGroupUiState(
    val isLoading: Boolean = false,
    val inviteCode: String = "",
) {
    val isValid: Boolean
        get() = inviteCode.length >= 8
}

sealed interface ParticipateGroupUiEvent {
    sealed interface Type : ParticipateGroupUiEvent {
        data class OnInviteCodeTyped(val text: String) : Type
    }
}

sealed interface ParticipateGroupUiEffect {

}