package site.dogether.data.remote.model.res.group

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.data.remote.model.res.group.GroupInfoResMapper.toDomainModel
import site.dogether.domain.model.group.JoiningGroups

@Serializable
data class GetJoiningGroupsRes(
    val lastSelectedGroupIndex: Int = -1,
    val joiningChallengeGroups: List<GroupInfoRes>,
) : DataModel

object GetJoiningGroupsResMapper : DataMapper<GetJoiningGroupsRes, JoiningGroups> {
    override fun GetJoiningGroupsRes.toDomainModel(): JoiningGroups {
        return JoiningGroups(
            lastSelectedGroupIndex = lastSelectedGroupIndex,
            groups = joiningChallengeGroups.map { it.toDomainModel() }
        )
    }
}
