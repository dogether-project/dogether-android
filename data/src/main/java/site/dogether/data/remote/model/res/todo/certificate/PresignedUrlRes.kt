package site.dogether.data.remote.model.res.todo.certificate

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.certificate.PresignedUrlData

/**
 * S3 Presigned URL 발급 응답 모델
 */
@Serializable
data class PresignedUrlRes(
    val presignedUrls: List<String>
) : DataModel

/**
 * PresignedUrlRes to PresignedUrlData 매퍼
 */
object PresignedUrlResMapper : DataMapper<PresignedUrlRes, PresignedUrlData> {
    override fun PresignedUrlRes.toDomainModel(): PresignedUrlData {
        return PresignedUrlData(presignedUrls = presignedUrls.map { it.split("?")[0] })
    }
}
