package site.dogether.data.remote.model.res.user

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.user.Profile

@Serializable
data class ProfileRes(
    val name: String,
    val profileImageUrl: String
) : DataModel

object ProfileResMapper : DataMapper<ProfileRes, Profile> {

    override fun ProfileRes.toDomainModel(): Profile {
        return Profile(
            name = name,
            profileImageUrl = profileImageUrl
        )
    }
}