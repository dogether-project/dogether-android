package site.dogether.presentation.screen.certificate.my_cert_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import site.dogether.KEY_DATE
import site.dogether.KEY_GROUP_ID
import site.dogether.KEY_TODO_INDEX
import site.dogether.common.utils.DateTimeUtils.DATE_FORMAT_FULL_YEAR_DASHED
import site.dogether.common.utils.DateTimeUtils.toLocalDate
import site.dogether.common.utils.DateTimeUtils.today
import site.dogether.common.utils.orZero
import site.dogether.domain.use_case.todo.GetMyTodosByDateUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error

class MyCertInfoViewModel(
    private val getMyTodoListByDate: GetMyTodosByDateUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<MyCertInfoUiState>(MyCertInfoUiState()) {

    private val groupId: Int by lazy {
        savedStateHandle.get<Int>(KEY_GROUP_ID).orZero()
    }

    private val focusedTodoIndex: Int by lazy {
        savedStateHandle.get<Int>(KEY_TODO_INDEX).orZero()
    }

    private val date: String by lazy {
        savedStateHandle.get<String>(KEY_DATE).orEmpty()
    }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    isLoading = true,
                    isToday = date.toLocalDate(DATE_FORMAT_FULL_YEAR_DASHED) == today
                )
            }

            getMyTodoListByDate(
                groupId = groupId,
                date = date
            ).onSuccess { todos ->
                updateState {
                    it.copy(
                        todos = todos.toImmutableList(),
                        selectedItemIndex = focusedTodoIndex,
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

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is MyCertInfoUiEvent.Click -> {
                when (event) {
                    is MyCertInfoUiEvent.Click.OnClickItem -> {
                        updateState { it.copy(selectedItemIndex = event.index) }
                    }

                    is MyCertInfoUiEvent.Click.OnClickCertificate -> {
                        val selectedTodo = uiState.todos[uiState.selectedItemIndex]

                        postEffect(
                            MyCertInfoUiEffect.NavigateToCertificateTodo(
                                todoId = selectedTodo.id,
                                todoTitle = selectedTodo.content
                            )
                        )
                    }
                }
            }

            is MyCertInfoUiEvent.Callback -> {
                when (event) {
                    is MyCertInfoUiEvent.Callback.OnSwipeLeft -> {
                        if (uiState.selectedItemIndex < uiState.todos.lastIndex) {
                            updateState { it.copy(selectedItemIndex = it.selectedItemIndex + 1) }
                        }
                    }

                    is MyCertInfoUiEvent.Callback.OnSwipeRight -> {
                        if (uiState.selectedItemIndex > 0) {
                            updateState { it.copy(selectedItemIndex = it.selectedItemIndex - 1) }
                        }
                    }
                }
            }
        }
    }
}