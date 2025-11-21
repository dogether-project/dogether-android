package site.dogether.domain.model.user

import site.dogether.domain.model.DomainModel

data class RankingMembers(
    val list: List<RankingMember>
) : DomainModel
