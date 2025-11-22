package site.dogether.data.remote.model.res.group

import kotlinx.serialization.Serializable
import site.dogether.common.utils.orZero
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.group.Group

@Serializable
data class GroupInfoRes(
    val groupId: Int?,
    val groupName: String?,
    val currentMemberCount: Int?,
    val maximumMemberCount: Int?,
    val joinCode: String?,
    val status: String?,
    val startAt: String?,
    val endAt: String?,
    val progressDay: Int?,
    val progressRate: Float?,
) : DataModel

object GroupInfoResMapper : DataMapper<GroupInfoRes, Group> {
    override fun GroupInfoRes.toDomainModel(): Group {
        return Group(
            id = groupId.orZero(),
            name = groupName.orEmpty(),
            currentMemberCount = currentMemberCount.orZero(),
            maximumMemberCount = maximumMemberCount.orZero(),
            joinCode = joinCode.orEmpty(),
            status = status.orEmpty(),
            startAt = startAt.orEmpty(),
            endAt = endAt.orEmpty(),
            progressDay = progressDay.orZero(),
            progressRate = progressRate.orZero()
        )
    }
}
