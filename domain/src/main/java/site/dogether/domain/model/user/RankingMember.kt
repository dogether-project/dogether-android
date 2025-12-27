package site.dogether.domain.model.user

import site.dogether.domain.model.DomainModel

data class RankingMember(
    val memberId: Int = 0,
    val rank: Int = 0,
    val profileImageUrl: String = "",
    val name: String = "",
    val historyReadStatus: String = "",
    val achievementRate: Int = 0
) : DomainModel
