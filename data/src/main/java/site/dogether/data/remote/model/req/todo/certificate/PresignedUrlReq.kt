package site.dogether.data.remote.model.req.todo.certificate

import kotlinx.serialization.Serializable

/**
 * S3 Presigned URL 발급 요청 모델
 */
@Serializable
data class PresignedUrlReq(
    val dailyTodoId: Int,
    val uploadFileTypes: List<String>
)

