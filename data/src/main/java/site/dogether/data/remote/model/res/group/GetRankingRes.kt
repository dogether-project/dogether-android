package site.dogether.data.remote.model.res.group

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.data.remote.model.res.group.RankingMemberResMapper.toDomainModel
import site.dogether.domain.model.user.RankingMembers

@Serializable
data class GetRankingRes(
    val ranking: List<RankingMemberRes>
) : DataModel

object GetRankingResMapper : DataMapper<GetRankingRes, RankingMembers> {
    override fun GetRankingRes.toDomainModel(): RankingMembers {
        return RankingMembers(ranking.map { it.toDomainModel() })
    }
}