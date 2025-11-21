package site.dogether.data.remote.model.res.group

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.user.RankingMember

@Serializable
data class RankingMemberRes(
    val memberId: Int,
    val rank: Int,
    val profileImageUrl: String,
    val name: String,
    val historyReadStatus: String,
    val achievementRate: Int
) : DataModel

object RankingMemberResMapper : DataMapper<RankingMemberRes, RankingMember> {
    override fun RankingMemberRes.toDomainModel(): RankingMember {
        return RankingMember(
            memberId = memberId,
            rank = rank,
            profileImageUrl = profileImageUrl,
            name = name,
            historyReadStatus = historyReadStatus,
            achievementRate = achievementRate
        )
    }
}
