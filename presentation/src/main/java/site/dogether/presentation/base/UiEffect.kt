package site.dogether.presentation.base

import site.dogether.presentation.screen.error.model.Error

interface UiEffect {
    data object NavigateToPreviousScreen : UiEffect

    data class NavigateTo(
        val screen: String,
        val clearBackStack: Boolean = false,
    ) : UiEffect

    /**
     * 에러 화면으로 이동하면서 콜백을 설정합니다.
     * @param error 표시할 에러 타입
     * @param onPositive positive 버튼 클릭 시 실행할 콜백 (예: 재시도)
     * @param onNegative negative 버튼 클릭 시 실행할 콜백 (예: 뒤로가기)
     */
    data class NavigateToErrorWithCallback(
        val error: Error,
        val onPositive: (() -> Unit)? = null,
        val onNegative: (() -> Unit)? = null,
    ) : UiEffect

    data class ShowToast(val text: String) : UiEffect
}