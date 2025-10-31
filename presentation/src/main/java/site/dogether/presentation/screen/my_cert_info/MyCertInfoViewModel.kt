package site.dogether.presentation.screen.my_cert_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.KEY_GROUP_ID
import site.dogether.KEY_TODO_INDEX
import site.dogether.common.utils.orZero
import site.dogether.domain.use_case.todo.GetMyTodosByDateUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.utils.DATE_FORMAT_FULL_YEAR_DASHED
import site.dogether.presentation.utils.toFormattedString
import site.dogether.presentation.utils.today
import java.time.LocalDate

class MyCertInfoViewModel(
    private val getMyTodoListByDate: GetMyTodosByDateUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<MyCertInfoUiState>(MyCertInfoUiState()) {

    private val groupId: Int by lazy {
        savedStateHandle.get<Int>(KEY_GROUP_ID).orZero()
    }

    private val focusedTodoIndex: Int by lazy {
        savedStateHandle.get<Int>(KEY_TODO_INDEX).orZero()
    }

    init {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            getMyTodoListByDate(
                groupId = groupId,
                date = today.toFormattedString(DATE_FORMAT_FULL_YEAR_DASHED)
            ).onSuccess { todos ->
                updateState {
                    it.copy(
                        todos = todos,
                        selectedItemIndex = focusedTodoIndex
                    )
                }
            }.onFailure {

            }
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
                }
            }
        }
    }
}