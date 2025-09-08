package site.dogether.data.remote.model.res.group

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.group.Group

@Serializable
data class GroupInfoRes(
    val groupId: Int,
    val groupName: String,
    val currentMemberCount: Int,
    val maximumMemberCount: Int,
    val joinCode: String,
    val status: String,
    val startAt: String,
    val endAt: String,
    val progressDay: Int,
    val progressRate: Float,
) : DataModel

object GroupInfoResMapper : DataMapper<GroupInfoRes, Group> {
    override fun GroupInfoRes.toDomainModel(): Group {
        return Group(
            id = groupId,
            name = groupName,
            currentMemberCount = currentMemberCount,
            maximumMemberCount = maximumMemberCount,
            joinCode = joinCode,
            status = status,
            startAt = startAt,
            endAt = endAt,
            progressDay = progressDay,
            progressRate = progressRate
        )
    }
}
