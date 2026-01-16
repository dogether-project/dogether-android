package site.dogether.presentation.screen.todo.certificate

import android.net.Uri
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class CertificateDescriptionUiState(
    val todoId: Int = -1,
    val filePath: String = "",
    val imageUri: Uri? = null,
    val description: String = "",
    val isLoading: Boolean = false,
) {
    val isDescriptionValid: Boolean = description.isNotBlank()
}

sealed interface CertificateDescriptionUiEvent : UiEvent {
    sealed interface Lifecycle : CertificateDescriptionUiEvent {
        data object OnStart : Lifecycle
    }

    data class UpdateDescription(val text: String) : CertificateDescriptionUiEvent
    data object SubmitCertificate : CertificateDescriptionUiEvent
}

sealed interface CertificateDescriptionSideEffect : UiEffect {
    data object Back : CertificateDescriptionSideEffect
    data class ShowToast(val text: String) : CertificateDescriptionSideEffect
    data object NavigateToNext : CertificateDescriptionSideEffect
    data object NavigateToHome : CertificateDescriptionSideEffect
}
