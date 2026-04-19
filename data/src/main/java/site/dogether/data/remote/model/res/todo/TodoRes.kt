package site.dogether.data.remote.model.res.todo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import site.dogether.common.utils.orZero
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.todo.Todo

@Serializable
data class TodoRes(
    val id: Long? = 0L,
    val todoId: Long? = 0L,
    val content: String? = "",
    val status: String? = "",
    val certificationContent: String? = "",
    val certificationMediaUrl: String? = "",
    val reviewFeedback: String? = "",
    val isRead: Boolean? = false,
    val canRequestCertification: Boolean? = false,
    val canRequestCertificationReview: Boolean? = false,
) : DataModel

object TodoResMapper : DataMapper<TodoRes, Todo> {
    override fun TodoRes.toDomainModel(): Todo {
        return Todo(
            id = if (id != null && id != 0L) id else todoId.orZero(),
            content = content.orEmpty(),
            status = status.orEmpty(),
            certificationContent = certificationContent.orEmpty(),
            certificationMediaUrl = try {
                certificationMediaUrl.orEmpty()
            } catch (e: Exception) {
                ""
            },
            reviewFeedback = reviewFeedback.orEmpty(),
            isRead = isRead ?: false,
            canRemindCertification = canRequestCertification ?: false,
            canRemindReview = canRequestCertificationReview ?: false
        )
    }
}
