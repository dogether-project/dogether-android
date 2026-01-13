package site.dogether.data.remote.model.res.todo

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.todo.PendingReviewCertification
import site.dogether.domain.model.todo.PendingReviewCertifications

@Serializable
data class PendingReviewCertificationsRes(
    val dailyTodoCertifications: List<PendingReviewCertificationRes> = emptyList(),
) : DataModel

@Serializable
data class PendingReviewCertificationRes(
    val id: Long = 0L,
    val content: String = "",
    val mediaUrl: String = "",
    val todoContent: String = "",
    val doer: String = "",
)

object PendingReviewCertificationsResMapper :
    DataMapper<PendingReviewCertificationsRes, PendingReviewCertifications> {
    override fun PendingReviewCertificationsRes.toDomainModel(): PendingReviewCertifications {
        return PendingReviewCertifications(
            certifications = dailyTodoCertifications.map { it.toDomainModel() }
        )
    }

    private fun PendingReviewCertificationRes.toDomainModel(): PendingReviewCertification {
        return PendingReviewCertification(
            id = id,
            content = content,
            mediaUrl = mediaUrl,
            todoContent = todoContent,
            doer = doer
        )
    }
}

