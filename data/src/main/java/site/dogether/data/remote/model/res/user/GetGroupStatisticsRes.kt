package site.dogether.data.remote.model.res.user

import kotlinx.serialization.Serializable
import site.dogether.common.utils.orZero
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.data.remote.model.res.user.GroupCertificationStatisticsResMapper.toDomainModel
import site.dogether.data.remote.model.res.user.GroupInfoForStatisticsResMapper.toDomainModel
import site.dogether.data.remote.model.res.user.RankingResMapper.toDomainModel
import site.dogether.data.remote.model.res.user.StatsResMapper.toDomainModel
import site.dogether.domain.model.group.Group
import site.dogether.domain.model.user.GroupCertificationStatistics
import site.dogether.domain.model.user.GroupStatistics
import site.dogether.domain.model.user.Ranking
import site.dogether.domain.model.user.Stats

@Serializable
data class GetGroupStatisticsRes(
    val groupInfo: GroupInfoForStatisticsRes,
    val certificationPeriods: List<GroupCertificationStatisticsRes>,
    val ranking: RankingRes,
    val stats: StatsRes
) : DataModel

object GetGroupStatisticsResMapper : DataMapper<GetGroupStatisticsRes, GroupStatistics> {
    override fun GetGroupStatisticsRes.toDomainModel(): GroupStatistics {
        return GroupStatistics(
            group = groupInfo.toDomainModel(),
            certificationPeriods = certificationPeriods.map { it.toDomainModel() },
            ranking = ranking.toDomainModel(),
            stats = stats.toDomainModel()
        )
    }
}

@Serializable
data class GroupInfoForStatisticsRes(
    val name: String?,
    val maximumMemberCount: Int?,
    val currentMemberCount: Int?,
    val joinCode: String?,
    val endAt: String?
) : DataModel

object GroupInfoForStatisticsResMapper : DataMapper<GroupInfoForStatisticsRes, Group> {
    override fun GroupInfoForStatisticsRes.toDomainModel(): Group {
        return Group(
            name = name.orEmpty(),
            maximumMemberCount = maximumMemberCount.orZero(),
            currentMemberCount = currentMemberCount.orZero(),
            joinCode = joinCode.orEmpty(),
            endAt = endAt.orEmpty()
        )
    }
}

@Serializable
data class GroupCertificationStatisticsRes(
    val day: Int,
    val createdCount: Int,
    val certificatedCount: Int,
    val certificationRate: Int
) : DataModel

object GroupCertificationStatisticsResMapper : DataMapper<GroupCertificationStatisticsRes, GroupCertificationStatistics> {
    override fun GroupCertificationStatisticsRes.toDomainModel(): GroupCertificationStatistics {
        return GroupCertificationStatistics(
            day = day,
            createdCount = createdCount,
            certificatedCount = certificatedCount,
            certificationRate = certificationRate
        )
    }
}

@Serializable
data class RankingRes(
    val totalMemberCount: Int?,
    val myRank: Int?
) : DataModel

object RankingResMapper : DataMapper<RankingRes, Ranking> {
    override fun RankingRes.toDomainModel(): Ranking {
        return Ranking(
            totalMemberCount = totalMemberCount.orZero(),
            myRank = myRank.orZero()
        )
    }
}

@Serializable
data class StatsRes(
    val certificatedCount: Int?,
    val approvedCount: Int?,
    val rejectedCount: Int?
) : DataModel

object StatsResMapper : DataMapper<StatsRes, Stats> {
    override fun StatsRes.toDomainModel(): Stats {
        return Stats(
            certificatedCount = certificatedCount.orZero(),
            approvedCount = approvedCount.orZero(),
            rejectedCount = rejectedCount.orZero()
        )
    }
}
