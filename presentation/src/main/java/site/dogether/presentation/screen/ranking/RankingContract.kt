package site.dogether.presentation.screen.ranking

import site.dogether.domain.model.user.RankingMember
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class RankingUiState(
    val isLoading: Boolean = false,
    val groupId: Int = 0,
    val inRankMembers: List<RankingMember> = listOf(
        RankingMember(),
        RankingMember(),
        RankingMember()
    ), // 스켈레톤 표시를 위한 더미 데이터
    val outRankMembers: List<RankingMember> = listOf()
)

sealed interface RankingUiEvent : UiEvent {

}

sealed interface RankingUiEffect : UiEffect {

}