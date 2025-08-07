package site.dogether.presentation.screen.home

data class HomeUiState(
    val isLoading: Boolean = false,
    val timerProgress: Float = 0f,
    val timerText: String = ""
)

sealed interface HomeUiEvent {

}

sealed interface HomeUiEffect {

}