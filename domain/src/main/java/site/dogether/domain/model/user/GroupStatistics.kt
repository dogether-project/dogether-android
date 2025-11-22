package site.dogether.domain.model.user

import site.dogether.domain.model.DomainModel
import site.dogether.domain.model.group.Group

data class GroupStatistics(
    val group: Group = Group(),
    val certificationPeriods: List<GroupCertificationStatistics> = listOf(),
    val ranking: Ranking = Ranking(),
    val stats: Stats = Stats()
) : DomainModel

data class GroupCertificationStatistics(
    val day: Int = 0,
    val createdCount: Int = 0,
    val certificatedCount: Int = 0,
    val certificationRate: Int = 0
) : DomainModel

data class Ranking(
    val totalMemberCount: Int = 0,
    val myRank: Int = 0,
) : DomainModel

data class Stats(
    val certificatedCount: Int = 0,
    val approvedCount: Int = 0,
    val rejectedCount: Int = 0
) : DomainModel