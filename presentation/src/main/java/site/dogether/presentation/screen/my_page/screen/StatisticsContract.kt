package site.dogether.presentation.screen.my_page.screen

data class StatisticsUiState(
    val isLoading: Boolean = false
)

sealed interface StatisticsUiEvent {

}

sealed interface StatisticsUiEffect {

}