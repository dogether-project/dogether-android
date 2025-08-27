package site.dogether.presentation.screen.participate_group

data class ParticipateGroupUiState(
    val isLoading: Boolean = false,
    val joinCode: String = "",
) {
    val isValid: Boolean
        get() = joinCode.length >= 8
}

sealed interface ParticipateGroupUiEvent {
    sealed interface Type : ParticipateGroupUiEvent {
        data class OnJoinCodeTyped(val text: String) : Type
    }
}

sealed interface ParticipateGroupUiEffect {

}