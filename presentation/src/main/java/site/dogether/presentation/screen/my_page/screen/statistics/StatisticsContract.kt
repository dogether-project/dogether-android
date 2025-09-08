package site.dogether.presentation.screen.my_page.screen.statistics

import site.dogether.domain.model.group.Group
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val groups: List<Group> = emptyList(),
    val selectedGroup: Group = Group(),
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