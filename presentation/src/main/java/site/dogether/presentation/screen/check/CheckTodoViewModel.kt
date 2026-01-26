package site.dogether.presentation.screen.check

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.todo.GetPendingReviewCertificationsUseCase
import site.dogether.domain.use_case.todo.ReviewTodoUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error

class CheckTodoViewModel(
    private val getPendingReviewCertifications: GetPendingReviewCertificationsUseCase,
    private val reviewTodo: ReviewTodoUseCase,
) : BaseViewModel<CheckTodoUiState>(CheckTodoUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is CheckTodoUiEvent.Lifecycle.OnStart -> {
                loadPendingReviewCertifications()
            }

            is CheckTodoUiEvent.SelectReviewType -> {
                updateState { it.copy(selectedReviewType = event.type) }

                // 노인정 선택 시 피드백 다이얼로그 표시
                if (event.type == ReviewType.REJECT) {
                    updateState { it.copy(isFeedbackDialogShowing = true) }
                } else {
                    updateState { it.copy(isFeedbackDialogShowing = false, reviewFeedback = "") }
                }
            }

            is CheckTodoUiEvent.ShowFeedbackDialog -> {
                updateState { it.copy(isFeedbackDialogShowing = true) }
            }

            is CheckTodoUiEvent.HideFeedbackDialog -> {
                updateState { it.copy(isFeedbackDialogShowing = false) }
            }

            is CheckTodoUiEvent.UpdateFeedback -> {
                updateState { it.copy(reviewFeedback = event.feedback) }
            }

            is CheckTodoUiEvent.SubmitReview -> {
                submitReview()
            }
        }
    }

    private fun loadPendingReviewCertifications() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }

            getPendingReviewCertifications().onSuccess { result ->
                updateState {
                    it.copy(
                        isLoading = false, certifications = result.certifications, currentIndex = 0, selectedReviewType = null, reviewFeedback = ""
                    )
                }
            }.onFailure {
                updateState { it.copy(isLoading = false) }
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { loadPendingReviewCertifications() }  // 재시도
                    )
                )
            }
        }
    }

    private fun submitReview() {
        val currentState = uiState
        if (currentState.selectedReviewType == null) {
            postEffect(UiEffect.ShowToast("검사 결과를 선택해주세요"))
            return
        }

        if (currentState.selectedReviewType == ReviewType.REJECT && currentState.reviewFeedback.isEmpty()) {
            postEffect(UiEffect.ShowToast("노인정 사유를 입력해주세요"))
            return
        }

        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }

            reviewTodo(
                todoId = currentState.todoId.toInt(), isApprove = currentState.selectedReviewType == ReviewType.APPROVE, feedback = if (currentState.selectedReviewType == ReviewType.REJECT) {
                    currentState.reviewFeedback
                } else {
                    ""
                }
            ).onSuccess {
                handleReviewSuccess()
            }.onFailure {
                updateState { it.copy(isLoading = false) }
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { submitReview() }  // 재시도
                    )
                )
            }
        }
    }

    private fun handleReviewSuccess() {
        val currentState = uiState

        if (currentState.hasMoreCertifications) {
            // 다음 인증으로 이동
            updateState {
                it.copy(
                    isLoading = false, currentIndex = it.currentIndex + 1, selectedReviewType = null, reviewFeedback = "", isFeedbackDialogShowing = false
                )
            }
            postEffect(UiEffect.ShowToast("검사 완료! 다음 인증을 확인하세요"))
        } else {
            // 모든 검사 완료
            updateState { it.copy(isLoading = false) }
            postEffect(CheckTodoUiEffect.ReviewSubmitted)
        }
    }
}
