package site.dogether.data.remote.model.res.user

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.user.ParticipatingInfo

@Serializable
data class CheckParticipatingRes(
    val checkParticipating: Boolean,
) : DataModel

object CheckParticipatingResMapper : DataMapper<CheckParticipatingRes, ParticipatingInfo> {

    override fun CheckParticipatingRes.toDomainModel(): ParticipatingInfo {
        return ParticipatingInfo(shouldParticipating = checkParticipating)
    }
}