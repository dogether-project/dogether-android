package site.dogether.presentation.screen.home

data class HomeUiState(
    val isLoading: Boolean = false,
)

sealed interface HomeUiEvent {

}

sealed interface HomeUiEffect {

}