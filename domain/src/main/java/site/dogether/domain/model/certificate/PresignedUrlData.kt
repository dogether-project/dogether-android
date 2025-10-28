package site.dogether.domain.model.certificate

import site.dogether.domain.model.DomainModel

/**
 * S3 Presigned URL 데이터 모델
 */
data class PresignedUrlData(
    val presignedUrls: List<String>
) : DomainModel
