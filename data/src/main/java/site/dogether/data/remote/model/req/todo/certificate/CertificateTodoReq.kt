package site.dogether.data.remote.model.req.todo.certificate

import kotlinx.serialization.Serializable

/**
 * 데일리 투두 인증 요청 모델
 */
@Serializable
data class CertificateTodoReq(
    val content: String,
    val mediaUrl: String
)
