package site.dogether.domain.model.todo

import site.dogether.domain.model.DomainModel

/**
 * 검사 대기 중인 인증 목록
 */
data class PendingReviewCertifications(
    val certifications: List<PendingReviewCertification> = emptyList(),
) : DomainModel

/**
 * 검사 대기 중인 인증 정보
 */
data class PendingReviewCertification(
    val id: Long = 0L,
    val content: String = "",
    val mediaUrl: String = "",
    val todoContent: String = "",
    val doer: String = "",
) : DomainModel

