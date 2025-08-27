package site.dogether.data.remote.model.res.group

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.group.CreatedGroupInfo

@Serializable
data class CreateGroupRes(
    val joinCode: String,
) : DataModel

object CreateGroupResMapper : DataMapper<CreateGroupRes, CreatedGroupInfo> {

    override fun CreateGroupRes.toDomainModel(): CreatedGroupInfo {
        return CreatedGroupInfo(joinCode = joinCode)
    }
}
