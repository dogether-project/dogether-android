package site.dogether.presentation.screen.my_page.screen.certification_list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.domain.model.todo.Todo.Companion.STATUS_APPROVE
import site.dogether.domain.model.todo.Todo.Companion.STATUS_REJECT
import site.dogether.domain.model.todo.Todo.Companion.STATUS_REVIEW_PENDING
import site.dogether.domain.use_case.todo.GetMyActivityUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.my_page.screen.certification_list.model.Chip

class CertificationListViewModel(
    private val getMyActivity: GetMyActivityUseCase
) : BaseViewModel<CertificationListUiState>(CertificationListUiState()) {

    init {
        loadMyActivityData(
            sortBy = uiState.selectedSortingMethod.serverString,
            status = null,
            page = 0
        )
    }

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is CertificationListUiEvent.Click -> {
                when (event) {
                    is CertificationListUiEvent.Click.OnClickSelectSortingMethod -> {
                        updateState { it.copy(isSelectSortingMethodBottomSheetShowing = true) }
                    }

                    is CertificationListUiEvent.Click.OnClickSortingMethod -> {
                        updateState {
                            it.copy(
                                selectedSortingMethod = event.sortingMethod,
                                isSelectSortingMethodBottomSheetShowing = false
                            )
                        }

                        loadMyActivityData(
                            sortBy = event.sortingMethod.serverString,
                            status = when (uiState.selectedChip) {
                                Chip.ReviewPending -> STATUS_REVIEW_PENDING
                                Chip.Approve -> STATUS_APPROVE
                                Chip.Reject -> STATUS_REJECT
                                else -> null
                            },
                            page = 0
                        )
                    }

                    is CertificationListUiEvent.Click.OnClickChip -> {
                        updateState {
                            it.copy(selectedChip = event.chip)
                        }
                        
                        loadMyActivityData(
                            sortBy = uiState.selectedSortingMethod.serverString,
                            status = when (event.chip) {
                                Chip.ReviewPending -> STATUS_REVIEW_PENDING
                                Chip.Approve -> STATUS_APPROVE
                                Chip.Reject -> STATUS_REJECT
                                else -> null
                            },
                            page = 0
                        )
                    }
                }
            }

            is CertificationListUiEvent.Callback -> {
                when (event) {
                    is CertificationListUiEvent.Callback.OnSelectSortingMethodBottomSheetDismissRequested -> {
                        updateState { it.copy(isSelectSortingMethodBottomSheetShowing = false) }
                    }
                }
            }
        }
    }

    private fun loadMyActivityData(
        sortBy: String,
        status: String? = null,
        page: Int
    ) {
        updateState { it.copy(isLoading = true) }

        viewModelScope.launch {
            getMyActivity(
                sortBy = sortBy,
                status = status,
                page = page
            ).onSuccess { myActivity ->
                updateState { it.copy(myActivity = myActivity) }
            }.onFailure {
                // handle exception
            }
        }.invokeOnCompletion {
            updateState { it.copy(isLoading = false) }
        }
    }
}