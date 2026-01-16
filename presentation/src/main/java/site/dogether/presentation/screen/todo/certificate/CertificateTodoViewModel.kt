package site.dogether.presentation.screen.todo.certificate

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import site.dogether.KEY_TODO_ID
import site.dogether.KEY_TODO_TITLE
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class CertificateTodoViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel<CertificateTodoUiState>(initialState = CertificateTodoUiState()) {

    val todoId: Int by lazy {
        savedStateHandle.get<Int>(KEY_TODO_ID) ?: -1
    }

    val todoTitle: String by lazy {
        savedStateHandle.get<String>(KEY_TODO_TITLE).orEmpty()
    }

    override fun onEvent(event: UiEvent) {
        when (event) {
            is CertificateTodoUiEvent.Lifecycle -> {
                updateState {
                    it.copy(
                        todoId = todoId, todoTitle = todoTitle
                    )
                }

                handleLifecycleEvent(event)
            }

            is CertificateTodoUiEvent.SelectFromGallery -> {
                postEffect(CertificateTodoSideEffect.OpenGallery)
            }

            is CertificateTodoUiEvent.TakePhoto -> {
                postEffect(CertificateTodoSideEffect.OpenCamera)
            }

            is CertificateTodoUiEvent.Next -> {
                if (uiState.selectedImageUri != null) {
                    postEffect(CertificateTodoSideEffect.NavigateToNext)
                } else {
                    postEffect(CertificateTodoSideEffect.ShowToast("인증 사진을 선택해주세요"))
                }
            }
        }
    }

    private fun handleLifecycleEvent(event: CertificateTodoUiEvent.Lifecycle) {
        when (event) {
            is CertificateTodoUiEvent.Lifecycle.OnStart -> {
                // 초기화 로직
            }
        }
    }

    fun setSelectedImage(uri: Uri?) {
        updateState { state ->
            state.copy(
                selectedImageUri = uri, isRequestingCamera = false, isRequestingGallery = false, errorMessage = null
            )
        }
    }

    fun setRequestingGallery(isRequesting: Boolean) {
        updateState { state ->
            state.copy(isRequestingGallery = isRequesting)
        }
    }

    fun setRequestingCamera(isRequesting: Boolean) {
        updateState { state ->
            state.copy(isRequestingCamera = isRequesting)
        }
    }

    fun setError(message: String?) {
        updateState { state ->
            state.copy(
                errorMessage = message, isRequestingCamera = false, isRequestingGallery = false
            )
        }
    }
}