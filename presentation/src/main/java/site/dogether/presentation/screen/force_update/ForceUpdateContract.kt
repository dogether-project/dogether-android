package site.dogether.presentation.screen.force_update

data class ForceUpdateUiState(
    val isLoading: Boolean = false
)

sealed interface ForceUpdateUiEvent {
    sealed interface Click : ForceUpdateUiEvent {
        data object OnClickUpdate : Click
    }
}

sealed interface ForceUpdateUiEffect {

}