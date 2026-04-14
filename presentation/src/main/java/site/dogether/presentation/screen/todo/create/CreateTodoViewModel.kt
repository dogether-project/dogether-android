package site.dogether.presentation.screen.todo.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import site.dogether.KEY_GROUP_ID
import site.dogether.KEY_SELECTED_DATE
import site.dogether.common.utils.DateTimeUtils.DATE_FORMAT_FULL_YEAR
import site.dogether.common.utils.DateTimeUtils.DATE_FORMAT_SHORT_YEAR
import site.dogether.common.utils.DateTimeUtils.toFormattedString
import site.dogether.common.utils.DateTimeUtils.toLocalDate
import site.dogether.common.utils.orZero
import site.dogether.domain.model.todo.Todo
import site.dogether.domain.use_case.todo.CreateMyTodosUseCase
import site.dogether.domain.use_case.todo.GetMyTodoSpecificDateUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.dialog_state.DialogState
import site.dogether.presentation.screen.error.model.Error

class CreateTodoViewModel(
    private val getMyTodoSpecificDateUseCase: GetMyTodoSpecificDateUseCase,
    private val createMyTodosUseCase: CreateMyTodosUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<CreateTodoUiState>(
    initialState = CreateTodoUiState()
) {

    private val groupId: Int by lazy {
        savedStateHandle.get<Int>(KEY_GROUP_ID).orZero()
    }

    private val selectedDate: String by lazy {
        savedStateHandle.get<String>(KEY_SELECTED_DATE).orEmpty()
    }

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is CreateTodoUiEvent.Lifecycle -> {
                updateState {
                    it.copy(
                        selectedDate = selectedDate.toLocalDate(DATE_FORMAT_SHORT_YEAR)
                    )
                }
                handleLifecycleEvent(event)
            }

            is CreateTodoUiEvent.UpdateTodoText -> {
                updateTodoText(event.text)
            }

            is CreateTodoUiEvent.RemoveTodoItem -> {
                removeTodoItem(event.index)
            }

            is CreateTodoUiEvent.AddTodoItem -> {
                addTodoItem()
            }

            is CreateTodoUiEvent.CreateTodos -> {
                showCheckPopup()
            }

            is CreateTodoUiEvent.CheckDialog -> {
                when (event) {
                    is CreateTodoUiEvent.CheckDialog.Confirm -> {
                        createTodos()
                    }

                    is CreateTodoUiEvent.CheckDialog.Cancel -> {
                        closeCheckPopup()
                    }
                }
            }
        }
    }

    private fun handleLifecycleEvent(event: CreateTodoUiEvent.Lifecycle) {
        when (event) {
            is CreateTodoUiEvent.Lifecycle.OnStart -> {
                // 등록된 투두 리스트 가져오기
                getMyTodoList()
            }
        }
    }

    private fun getMyTodoList() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }

            getMyTodoSpecificDateUseCase(
                groupId = groupId,
                date = uiState.selectedDate.toFormattedString(DATE_FORMAT_FULL_YEAR)
            )
                .getOrElse {
                    postEffect(
                        UiEffect.NavigateToErrorWithCallback(
                            error = Error.LoadData,
                            onPositive = { getMyTodoList() }
                        )
                    )
                    return@launch
                }.let { todoList ->
                    updateState { state ->
                        state.copy(todoItems = todoList.todos.toImmutableList())
                    }
                }
        }.invokeOnCompletion {
            updateState { it.copy(isLoading = false) }
        }
    }

    // 임시 아이템 삭제
    private fun removeTodoItem(index: Int) {
        updateState { state ->
            if (state.todoItems.size > 1 && index < state.todoItems.size) {
                val updatedItems = state.todoItems.toMutableList()
                updatedItems.removeAt(index)
                state.copy(
                    todoItems = updatedItems.toImmutableList(),
                    ctaEnabled = updatedItems.any { it.id == 0L }
                )
            } else {
                state
            }
        }
    }

    private fun updateTodoText(text: String) {
        updateState { state ->
            state.copy(
                todoText = text,
                addEnabled = text.isNotBlank() && state.todoItems.map { it.content }.none { it == text }
            )
        }
    }

    private fun addTodoItem() {
        updateState { state ->
            if (state.todoItems.size < state.maxTodoCount) {
                val updatedItems = state.todoItems.toMutableList()
                updatedItems.add(Todo(content = state.todoText))
                state.copy(
                    todoItems = updatedItems.toImmutableList(),
                    todoText = "",
                    ctaEnabled = updatedItems.any { it.id == 0L }
                )
            } else {
                state
            }
        }
    }

    private fun showCheckPopup() {
        updateState {
            it.copy(
                createTodoCheckDialogState = DialogState(true)
            )
        }
    }

    private fun closeCheckPopup() {
        updateState {
            it.copy(
                createTodoCheckDialogState = DialogState(false)
            )
        }
    }

    private fun createTodos() {
        viewModelScope.launch {
            createMyTodosUseCase(
                groupId = groupId,
                todos = uiState.todoItems.filter { it.id == 0L }.map { it.content }
            ).getOrElse {
                closeCheckPopup()
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { createTodos() }
                    )
                )
                return@launch
            }.let {
                closeCheckPopup()
                getMyTodoList()
                postEffect(UiEffect.ShowToast("투두 저장에 성공했습니다"))
            }
        }
    }
}