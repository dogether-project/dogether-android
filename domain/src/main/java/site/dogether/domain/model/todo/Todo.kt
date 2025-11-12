package site.dogether.domain.model.todo

import site.dogether.domain.model.DomainModel

data class Todo(
    val id: Long = 0L,
    val content: String = "",
    val status: String = "",
    val certificationContent: String = "",
    val certificationMediaUrl: String = "",
    val reviewFeedback: String = "",
) : DomainModel {
    companion object {
        const val STATUS_CERTIFY_PENDING = "CERTIFY_PENDING"
        const val STATUS_REVIEW_PENDING = "REVIEW_PENDING"
        const val STATUS_APPROVE = "APPROVE"
        const val STATUS_REJECT = "REJECT"
    }
}