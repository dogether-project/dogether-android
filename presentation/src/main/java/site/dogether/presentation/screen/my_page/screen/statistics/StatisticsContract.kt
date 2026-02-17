package site.dogether.presentation.screen.my_page.screen.statistics

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import site.dogether.domain.model.group.Group
import site.dogether.domain.model.user.GroupStatistics
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val groups: ImmutableList<Group> = persistentListOf(),
    val selectedGroup: Group = Group(),
    val isSelectGroupBottomSheetShowing: Boolean = false,
    val groupStatistics: GroupStatistics = GroupStatistics()
)

sealed interface StatisticsUiEvent : UiEvent {
    sealed interface Click : StatisticsUiEvent {
        data object OnClickSelectGroup : Click

        data class OnClickGroupItem(val group: Group) : Click
    }

    sealed interface Callback : StatisticsUiEvent {
        data object OnSelectGroupBottomSheetDismissRequested : Callback
    }
}

sealed interface StatisticsUiEffect : UiEffect {

}