package site.dogether.presentation.screen.my_page.screen.statistics

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val groups: List<String> = listOf("DND 작심삼일 탈출러"),
    val currentGroup: String = "DND 작심삼일 탈출러",
    val groupList: List<String> = listOf("DND 작심삼일 탈출러", "DND 작심삼일 탈출러"),
    val isChooseGroupBottomSheetExpanded: Boolean = true,
)

sealed interface StatisticsUiEvent {
    sealed interface Click : StatisticsUiEvent {
        data object OnClickChooseGroup : Click
    }

    sealed interface Callback : StatisticsUiEvent {
        data object OnChooseGroupBottomSheetDismissRequested : Callback
    }
}

sealed interface StatisticsUiEffect {

}