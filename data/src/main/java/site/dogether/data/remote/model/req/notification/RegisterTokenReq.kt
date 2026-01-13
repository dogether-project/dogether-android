package site.dogether.data.remote.model.req.notification

import kotlinx.serialization.Serializable

@Serializable
data class RegisterTokenReq(
    val token: String
)

