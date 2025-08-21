package site.dogether.presentation.screen.my_page.screen.statistics

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val groups: List<String> = listOf("DND 작심삼일 탈출러"),
    val selectedGroup: String = "DND 작심삼일 탈출러",
    val groupList: List<String> = listOf("DND 작심삼일 탈출러", "DND 작심삼일 탈출러"),
    val isSelectGroupBottomSheetExpanded: Boolean = true,
)

sealed interface StatisticsUiEvent {
    sealed interface Click : StatisticsUiEvent {
        data object OnClickSelectGroup : Click
    }

    sealed interface Callback : StatisticsUiEvent {
        data object OnSelectGroupBottomSheetDismissRequested : Callback
    }
}

sealed interface StatisticsUiEffect {

}