package site.dogether.presentation.screen.check

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import site.dogether.common.utils.orZero
import site.dogether.domain.model.todo.PendingReviewCertification
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

@Immutable
data class CheckTodoUiState(
    val isLoading: Boolean = false,
    val certifications: ImmutableList<PendingReviewCertification> = persistentListOf(),
    val currentIndex: Int = 0,
    val selectedReviewType: ReviewType? = null,
    val reviewFeedback: String = "",
    val isFeedbackDialogShowing: Boolean = false,
    val errorMessage: String? = null
) {
    val currentCertification: PendingReviewCertification?
        get() = certifications.getOrNull(currentIndex)

    val todoId: Long
        get() = currentCertification?.id.orZero()

    val todoTitle: String
        get() = currentCertification?.todoContent.orEmpty()

    val certificationContent: String
        get() = currentCertification?.content.orEmpty()

    val certificationMediaUrl: String
        get() = currentCertification?.mediaUrl.orEmpty()

    val memberName: String
        get() = currentCertification?.doer.orEmpty()

    val hasMoreCertifications: Boolean
        get() = currentIndex < certifications.size - 1

    val isEmpty: Boolean
        get() = certifications.isEmpty()

    val confirmEnabled: Boolean
        get() = selectedReviewType != null && reviewFeedback.isNotEmpty()
}

enum class ReviewType {
    APPROVE, // 인정
    REJECT,  // 노인정
}

sealed interface CheckTodoUiEvent : UiEvent {
    sealed interface Lifecycle : CheckTodoUiEvent {
        data object OnStart : Lifecycle
    }

    data class SelectReviewType(val type: ReviewType) : CheckTodoUiEvent
    data object ShowFeedbackDialog : CheckTodoUiEvent
    data object HideFeedbackDialog : CheckTodoUiEvent
    data class UpdateFeedback(val feedback: String) : CheckTodoUiEvent
    data object SubmitReview : CheckTodoUiEvent
}

sealed interface CheckTodoUiEffect : UiEffect {

}