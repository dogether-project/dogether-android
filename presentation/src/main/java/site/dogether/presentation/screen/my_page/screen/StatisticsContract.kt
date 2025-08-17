package site.dogether.presentation.screen.my_page.screen

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val groups: List<String> = listOf("DND 작심삼일 탈출러"),
    val currentGroup: String = "DND 작심삼일 탈출러",
)

sealed interface StatisticsUiEvent {

}

sealed interface StatisticsUiEffect {

}