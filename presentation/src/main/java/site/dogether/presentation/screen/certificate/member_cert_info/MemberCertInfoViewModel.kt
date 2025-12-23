package site.dogether.presentation.screen.certificate.member_cert_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.KEY_GROUP_ID
import site.dogether.KEY_MEMBER_ID
import site.dogether.KEY_MEMBER_NAME
import site.dogether.common.utils.orZero
import site.dogether.domain.use_case.todo.GetMemberTodoHistoryUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class MemberCertInfoViewModel(
    private val getMemberTodoHistory: GetMemberTodoHistoryUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<MemberCertInfoUiState>(MemberCertInfoUiState()) {

    private val groupId: Int by lazy {
        savedStateHandle.get<Int>(KEY_GROUP_ID).orZero()
    }

    private val memberId: Int by lazy {
        savedStateHandle.get<Int>(KEY_MEMBER_ID).orZero()
    }

    private val name : String by lazy {
        savedStateHandle.get<String>(KEY_MEMBER_NAME).orEmpty()
    }

    init {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, name = name) }

            getMemberTodoHistory(
                groupId = groupId,
                memberId = memberId
            ).onSuccess { memberTodoHistory ->
                val initialIndex = memberTodoHistory.currentTodoHistoryToReadIndex
                updateState {
                    it.copy(
                        todos = memberTodoHistory.todos,
                        selectedItemIndex = if (initialIndex < memberTodoHistory.todos.size) {
                            initialIndex
                        } else {
                            0
                        },
                        isLoading = false
                    )
                }
            }.onFailure {
                updateState { it.copy(isLoading = false) }
            }
        }
    }

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is MemberCertInfoUiEvent.Click -> {
                when (event) {
                    is MemberCertInfoUiEvent.Click.OnClickItem -> {
                        updateState { it.copy(selectedItemIndex = event.index) }
                    }
                }
            }

            is MemberCertInfoUiEvent.Callback -> {
                when (event) {
                    is MemberCertInfoUiEvent.Callback.OnSwipeLeft -> {
                        if (uiState.selectedItemIndex < uiState.todos.lastIndex) {
                            updateState { it.copy(selectedItemIndex = it.selectedItemIndex + 1) }
                        }
                    }

                    is MemberCertInfoUiEvent.Callback.OnSwipeRight -> {
                        if (uiState.selectedItemIndex > 0) {
                            updateState { it.copy(selectedItemIndex = it.selectedItemIndex - 1) }
                        }
                    }
                }
            }
        }
    }
}

