package site.dogether.data.model

import kotlinx.serialization.Serializable

@Serializable
data class JwtPayload(
    val sub: String,
)
