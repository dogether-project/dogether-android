package site.dogether.data.remote.model.req.todo.review

import kotlinx.serialization.Serializable

/**
 * 투두 검사 요청 모델
 */
@Serializable
data class ReviewTodoReq(
    val result: String,
    val reviewFeedback: String
)

enum class ReviewTodoResult {
    APPROVE, REJECT;

    companion object {
        fun getResults(isApproved: Boolean): String {
            return (APPROVE.takeIf { isApproved } ?: REJECT).name
        }
    }
}

