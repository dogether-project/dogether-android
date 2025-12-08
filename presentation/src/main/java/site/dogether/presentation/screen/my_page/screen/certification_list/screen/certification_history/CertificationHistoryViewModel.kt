package site.dogether.presentation.screen.my_page.screen.certification_list.screen.certification_history

import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class CertificationHistoryViewModel : BaseViewModel<CertificationHistoryUiState>(CertificationHistoryUiState()) {

    // todo: groupId 혹은 날짜 + itemIndex 전달

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is CertificationHistoryUiEvent.Click -> {
                when (event) {
                    is CertificationHistoryUiEvent.Click.OnClickItem -> {
                        updateState { it.copy(selectedItemIndex = event.index) }
                    }

                    is CertificationHistoryUiEvent.Click.OnClickCertificate -> {
                        val selectedTodo = uiState.todos[uiState.selectedItemIndex]

                    }
                }
            }

            is CertificationHistoryUiEvent.Callback -> {
                when (event) {
                    is CertificationHistoryUiEvent.Callback.OnSwipeLeft -> {
                        if (uiState.selectedItemIndex < uiState.todos.lastIndex) {
                            updateState { it.copy(selectedItemIndex = it.selectedItemIndex + 1) }
                        }
                    }

                    is CertificationHistoryUiEvent.Callback.OnSwipeRight -> {
                        if (uiState.selectedItemIndex > 0) {
                            updateState { it.copy(selectedItemIndex = it.selectedItemIndex - 1) }
                        }
                    }
                }
            }
        }
    }
}