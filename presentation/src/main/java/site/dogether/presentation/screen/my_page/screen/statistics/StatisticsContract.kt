package site.dogether.presentation.screen.my_page.screen.statistics

import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val groups: List<String> = listOf("DND 작심삼일 탈출러"),
    val selectedGroup: String = "DND 작심삼일 탈출러",
    val groupList: List<String> = listOf("DND 작심삼일 탈출러", "DND 작심삼일 탈출러"),
    val isSelectGroupBottomSheetShowing: Boolean = false,
)

sealed interface StatisticsUiEvent : UiEvent {
    sealed interface Click : StatisticsUiEvent {
        data object OnClickSelectGroup : Click
    }

    sealed interface Callback : StatisticsUiEvent {
        data object OnSelectGroupBottomSheetDismissRequested : Callback
    }
}

sealed interface StatisticsUiEffect : UiEffect {

}