package site.dogether.domain.model.certificate

import site.dogether.domain.model.DomainModel

/**
 * 데일리 투두 인증 도메인 모델
 */
data class CertifyTodoData(
    val code: String,
    val message: String
) : DomainModel
