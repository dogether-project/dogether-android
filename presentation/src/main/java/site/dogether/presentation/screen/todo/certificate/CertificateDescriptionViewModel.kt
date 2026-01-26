package site.dogether.presentation.screen.todo.certificate

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import site.dogether.KEY_ENCODED_URI
import site.dogether.KEY_TODO_ID
import site.dogether.domain.use_case.todo.CertificateTodoUseCase
import site.dogether.domain.use_case.todo.GetPresignedUrlsUseCase
import site.dogether.domain.use_case.todo.UploadImageToS3UseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error
import java.net.URLDecoder

class CertificateDescriptionViewModel(
    savedStateHandle: SavedStateHandle,
    private val getPresignedUrlsUseCase: GetPresignedUrlsUseCase,
    private val uploadImageToS3UseCase: UploadImageToS3UseCase,
    private val certificateTodoUseCase: CertificateTodoUseCase
) : BaseViewModel<CertificateDescriptionUiState>(
    initialState = CertificateDescriptionUiState()
) {

    val todoId: Int by lazy {
        savedStateHandle.get<Int>(KEY_TODO_ID) ?: -1
    }

    val encodedUri: String by lazy {
        savedStateHandle.get<String>(KEY_ENCODED_URI).orEmpty()
    }

    override fun onEvent(event: UiEvent) {
        when (event) {
            is CertificateDescriptionUiEvent.Lifecycle -> {
                updateState { state ->
                    state.copy(
                        todoId = todoId,
                        filePath = encodedUri
                    )
                }

                // URL 디코딩하여 URI로 변환
                runCatching {
                    val decodedUri = URLDecoder.decode(encodedUri, "UTF-8")
                    val uri = decodedUri.toUri()
                    setImageUri(uri)
                }

                handleLifecycleEvent(event)
            }

            is CertificateDescriptionUiEvent.UpdateDescription -> {
                updateDescription(event.text)
            }

            is CertificateDescriptionUiEvent.SubmitCertificate -> {
                submitCertificate()
            }
        }
    }

    private fun handleLifecycleEvent(event: CertificateDescriptionUiEvent.Lifecycle) {
        when (event) {
            is CertificateDescriptionUiEvent.Lifecycle.OnStart -> {
                // 초기화 로직
            }
        }
    }

    fun setImageUri(uri: Uri?) {
        updateState { state ->
            state.copy(imageUri = uri)
        }
    }

    private fun updateDescription(text: String) {
        updateState { state ->
            state.copy(description = text)
        }
    }

    private fun submitCertificate() {
        updateState { state ->
            state.copy(isLoading = true)
        }

        // 1. S3 Presigned URL 발급
        viewModelScope.launch {
            getPresignedUrlsUseCase(
                dailyTodoId = uiState.todoId,
                fileCount = 1
            ).fold(
                onSuccess = { presignedUrls ->
                    // 2. 이미지를 S3에 업로드
                    uploadImageToS3(presignedUrls.firstOrNull())
                },
                onFailure = {
                    updateState { state ->
                        state.copy(isLoading = false)
                    }
                    postEffect(
                        UiEffect.NavigateToErrorWithCallback(
                            error = Error.LoadData,
                            onPositive = { submitCertificate() }
                        )
                    )
                }
            )
        }
    }

    private fun uploadImageToS3(presignedUrl: String?) {
        if (presignedUrl == null) {
            updateState { state ->
                state.copy(isLoading = false)
            }
            postEffect(UiEffect.ShowToast("Presigned URL이 없습니다"))
            return
        }

        val imageUri = uiState.imageUri
        if (imageUri == null) {
            updateState { state ->
                state.copy(isLoading = false)
            }
            postEffect(UiEffect.ShowToast("이미지가 선택되지 않았습니다"))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            uploadImageToS3UseCase(
                presignedUrl = presignedUrl,
                imageUri = imageUri.toString()
            ).fold(
                onSuccess = {
                    certificateTodo(presignedUrl)
                },
                onFailure = {
                    updateState { state ->
                        state.copy(isLoading = false)
                    }
                    postEffect(
                        UiEffect.NavigateToErrorWithCallback(
                            error = Error.LoadData,
                            onPositive = { uploadImageToS3(presignedUrl) }
                        )
                    )
                }
            )
        }
    }

    private fun certificateTodo(s3Url: String) {
        viewModelScope.launch {
            certificateTodoUseCase(
                dailyTodoId = uiState.todoId,
                content = uiState.description,
                mediaUrl = s3Url
            ).fold(
                onSuccess = {
                    updateState { state ->
                        state.copy(isLoading = false)
                    }
                    postEffect(UiEffect.ShowToast("인증을 완료했어요!"))
                    postEffect(CertificateDescriptionSideEffect.NavigateToHome)
                },
                onFailure = {
                    updateState { state ->
                        state.copy(isLoading = false)
                    }
                    postEffect(
                        UiEffect.NavigateToErrorWithCallback(
                            error = Error.LoadData,
                            onPositive = { certificateTodo(s3Url) }
                        )
                    )
                }
            )
        }
    }
}
