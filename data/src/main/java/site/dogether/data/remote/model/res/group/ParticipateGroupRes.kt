package site.dogether.data.remote.model.res.group

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.group.ParticipateGroupInfo

@Serializable
data class ParticipateGroupRes(
    val groupName: String,
    val duration: Int,
    val maximumMemberCount: Int,
    val startAt: String,
    val endAt: String,
) : DataModel

object ParticipateGroupResMapper : DataMapper<ParticipateGroupRes, ParticipateGroupInfo> {
    override fun ParticipateGroupRes.toDomainModel(): ParticipateGroupInfo {
        return ParticipateGroupInfo(
            groupName = groupName,
            duration = duration,
            maximumMemberCount = maximumMemberCount,
            startAt = startAt,
            endAt = endAt
        )
    }
}
