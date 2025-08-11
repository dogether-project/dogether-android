package site.dogether.data.remote.model.res.user

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.user.UserInfo

@Serializable
data class UserInfoRes(
    val name: String,
    val accessToken: String,
) : DataModel

object UserInfoResMapper : DataMapper<UserInfoRes, UserInfo> {

    override fun UserInfoRes.toDomainModel(): UserInfo {
        return UserInfo(
            name = name,
            accessToken = accessToken
        )
    }
}