package site.dogether.presentation.screen.my_page.screen.statistics

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import site.dogether.domain.model.group.Group
import site.dogether.domain.model.user.GroupCertificationStatistics
import site.dogether.domain.model.user.GroupStatistics
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

@Immutable
data class StatisticsUiState(
    val isGroupsLoading: Boolean = false,
    val isStatisticsLoading: Boolean = false,
    val groups: ImmutableList<Group> = persistentListOf(),
    val selectedGroup: Group = Group(),
    val isSelectGroupBottomSheetShowing: Boolean = false,
    val groupStatistics: GroupStatistics = GroupStatistics(),
    val displayCertificationPeriods: ImmutableList<GroupCertificationStatistics> = persistentListOf()
)

sealed interface StatisticsUiEvent : UiEvent {
    sealed interface Click : StatisticsUiEvent {
        data object OnClickSelectGroup : Click

        data class OnClickGroupItem(val group: Group) : Click

        data object OnClickCreateGroup : Click
    }

    sealed interface Callback : StatisticsUiEvent {
        data object OnSelectGroupBottomSheetDismissRequested : Callback
    }
}

sealed interface StatisticsUiEffect : UiEffect {

}