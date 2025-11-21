package site.dogether.domain.model.user

import site.dogether.domain.model.DomainModel

data class RankingMember(
    val memberId: Int,
    val rank: Int,
    val profileImageUrl: String,
    val name: String,
    val historyReadStatus: String,
    val achievementRate: Int
) : DomainModel
