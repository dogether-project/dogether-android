package site.dogether.presentation.screen.todo.certificate

import android.net.Uri
import androidx.compose.runtime.Immutable
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

@Immutable
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

sealed interface CertificateDescriptionUiEffect : UiEffect {
    data object Back : CertificateDescriptionUiEffect

    data object NavigateToNext : CertificateDescriptionUiEffect

    data object NavigateToHome : CertificateDescriptionUiEffect
}
