package site.dogether.presentation.screen.todo.certificate

import android.net.Uri
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class CertificateTodoUiState(
    val todoId: Int = -1,
    val todoTitle: String = "",
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val isRequestingGallery: Boolean = false,
    val isRequestingCamera: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface CertificateTodoUiEvent : UiEvent {
    sealed interface Lifecycle : CertificateTodoUiEvent {
        data object OnStart : Lifecycle
    }

    data object SelectFromGallery : CertificateTodoUiEvent
    data object TakePhoto : CertificateTodoUiEvent
    data object Next : CertificateTodoUiEvent
}

sealed interface CertificateTodoSideEffect : UiEffect {
    data object Back : CertificateTodoSideEffect
    data object OpenGallery : CertificateTodoSideEffect
    data object OpenCamera : CertificateTodoSideEffect
    data object NavigateToNext : CertificateTodoSideEffect
}