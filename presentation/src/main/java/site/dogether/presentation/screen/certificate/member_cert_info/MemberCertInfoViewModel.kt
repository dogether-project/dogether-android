package site.dogether.presentation.screen.certificate.member_cert_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import site.dogether.KEY_GROUP_ID
import site.dogether.KEY_MEMBER_ID
import site.dogether.KEY_MEMBER_NAME
import site.dogether.common.utils.orZero
import site.dogether.domain.use_case.todo.GetMemberTodoHistoryUseCase
import site.dogether.domain.use_case.todo.ReadTodoUseCase
import site.dogether.domain.use_case.todo.RemindTodoUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error
import java.time.LocalDate

class MemberCertInfoViewModel(
    private val getMemberTodoHistory: GetMemberTodoHistoryUseCase,
    private val readTodoUseCase: ReadTodoUseCase,
    private val remindTodoUseCase: RemindTodoUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<MemberCertInfoUiState>(MemberCertInfoUiState()) {

    private val groupId: Int by lazy {
        savedStateHandle.get<Int>(KEY_GROUP_ID).orZero()
    }

    private val memberId: Int by lazy {
        savedStateHandle.get<Int>(KEY_MEMBER_ID).orZero()
    }

    private val name: String by lazy {
        savedStateHandle.get<String>(KEY_MEMBER_NAME).orEmpty()
    }

    init {
        loadData()
    }

    override fun onDateChanged(date: LocalDate) {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, name = name) }

            getMemberTodoHistory(
                groupId = groupId,
                memberId = memberId
            ).onSuccess { memberTodoHistory ->
                val initialIndex = memberTodoHistory.currentTodoHistoryToReadIndex
                val safeIndex = if (initialIndex < memberTodoHistory.todos.size) {
                    initialIndex
                } else {
                    0
                }

                val processedTodos = memberTodoHistory.todos.toMutableList()
                if (processedTodos.isNotEmpty()) {
                    val todoToRead = processedTodos[safeIndex]
                    if (!todoToRead.isRead) {
                        processedTodos[safeIndex] = todoToRead.copy(isRead = true)
                        viewModelScope.launch {
                            readTodoUseCase(todoToRead.id)
                        }
                    }
                }

                updateState {
                    it.copy(
                        todos = processedTodos.toImmutableList(),
                        selectedItemIndex = safeIndex,
                        isMine = memberTodoHistory.isMine
                    )
                }
            }.onFailure {
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { loadData() }
                    )
                )
            }
        }.invokeOnCompletion {
            updateState { it.copy(isLoading = false) }
        }
    }

    private fun readAndMark(index: Int) {
        if (index !in uiState.todos.indices) return

        val todoToRead = uiState.todos[index]
        if (!todoToRead.isRead) {
            val updatedTodos = uiState.todos.toMutableList()
            updatedTodos[index] = todoToRead.copy(isRead = true)

            updateState { it.copy(todos = updatedTodos.toImmutableList()) }

            viewModelScope.launch {
                readTodoUseCase(todoToRead.id)
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
                        readAndMark(event.index)
                    }

                    is MemberCertInfoUiEvent.Click.OnClickRemind -> {
                        remindTodo(event.reminderType)
                    }
                }
            }

            is MemberCertInfoUiEvent.Callback -> {
                when (event) {
                    is MemberCertInfoUiEvent.Callback.OnSwipeLeft -> {
                        if (uiState.selectedItemIndex < uiState.todos.lastIndex) {
                            val newIndex = uiState.selectedItemIndex + 1
                            updateState { it.copy(selectedItemIndex = newIndex) }
                            readAndMark(newIndex)
                        }
                    }

                    is MemberCertInfoUiEvent.Callback.OnSwipeRight -> {
                        if (uiState.selectedItemIndex > 0) {
                            val newIndex = uiState.selectedItemIndex - 1
                            updateState { it.copy(selectedItemIndex = newIndex) }
                            readAndMark(newIndex)
                        }
                    }
                }
            }
        }
    }

    private fun remindTodo(reminderType: String) {
        val selectedIndex = uiState.selectedItemIndex
        if (selectedIndex !in uiState.todos.indices) return
        val todoId = uiState.todos[selectedIndex].id

        viewModelScope.launch {
            remindTodoUseCase(
                todoId = todoId,
                reminderType = reminderType
            )
                .onSuccess {
                    showToast(
                        if (reminderType == "TODO_CERTIFICATION") "인증 재촉하기를 완료했어요!"
                        else "검사 재촉하기를 완료했어요!"
                    )
                    
                    updateState { state ->
                        val updatedTodos = state.todos.map { todo ->
                            if (todo.id == todoId) {
                                if (reminderType == "TODO_CERTIFICATION") {
                                    todo.copy(canRemindCertification = false)
                                } else {
                                    todo.copy(canRemindReview = false)
                                }
                            } else {
                                todo
                            }
                        }
                        state.copy(todos = updatedTodos.toImmutableList())
                    }
                }
                .onFailure {
                    showToast("요청에 실패했어요.")
                }
        }
    }
}

