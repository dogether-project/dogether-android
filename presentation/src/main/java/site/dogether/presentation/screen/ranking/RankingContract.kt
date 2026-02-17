package site.dogether.presentation.screen.ranking

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import site.dogether.domain.model.user.RankingMember
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

@Immutable
data class RankingUiState(
    val isLoading: Boolean = false,
    val groupId: Int = 0,
    val inRankMembers: ImmutableList<RankingMember> = persistentListOf(
        RankingMember(),
        RankingMember(),
        RankingMember()
    ), // 스켈레톤 표시를 위한 더미 데이터
    val outRankMembers: ImmutableList<RankingMember> = persistentListOf()
)

sealed interface RankingUiEvent : UiEvent {

}

sealed interface RankingUiEffect : UiEffect {

}