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
        loadNewMyActivityData(
            sortBy = uiState.selectedSortingMethod.serverString,
            status = null
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

                        loadNewMyActivityData(
                            sortBy = event.sortingMethod.serverString,
                            status = chipToStatus(uiState.selectedChip)
                        )
                    }

                    is CertificationListUiEvent.Click.OnClickChip -> {
                        updateState {
                            it.copy(selectedChip = event.chip)
                        }

                        loadNewMyActivityData(
                            sortBy = uiState.selectedSortingMethod.serverString,
                            status = chipToStatus(event.chip)
                        )
                    }

                    is CertificationListUiEvent.Click.OnClickCertificationInfo -> {
                        updateState {
                            it.copy(
                                isDetailMode = true,
                                detailedCertifications = event.detailedCertifications,
                                selectedItemIndex = event.index
                            )
                        }
                    }

                    is CertificationListUiEvent.Click.OnClickBackButtonWhenDetailMode -> {
                        updateState { it.copy(isDetailMode = false) }
                    }
                }
            }

            is CertificationListUiEvent.Callback -> {
                when (event) {
                    is CertificationListUiEvent.Callback.OnSelectSortingMethodBottomSheetDismissRequested -> {
                        updateState { it.copy(isSelectSortingMethodBottomSheetShowing = false) }
                    }

                    is CertificationListUiEvent.Callback.OnScrollReachedBottom -> {
                        updateState { it.copy(isLoading = true) }

                        viewModelScope.launch {
                            getMyActivity(
                                sortBy = uiState.selectedSortingMethod.serverString,
                                status = chipToStatus(uiState.selectedChip),
                                page = uiState.myActivity.pageInfo.recentPageNumber + 1
                            ).onSuccess { myActivity ->
                                if (myActivity.certificationsGroupedByTodoCompletedAt.isNotEmpty()) {
                                    val newList = myActivity.certificationsGroupedByTodoCompletedAt
                                    val existingList = uiState.myActivity.certificationsGroupedByTodoCompletedAt.toMutableList()

                                    if (existingList.isNotEmpty() && newList.isNotEmpty() &&
                                        existingList.last().createdAt == newList.first().createdAt
                                    ) {
                                        val lastItem = existingList.last()
                                        val updatedCertifications = lastItem.certificationInfo + newList.first().certificationInfo
                                        existingList[existingList.size - 1] = lastItem.copy(certificationInfo = updatedCertifications)

                                        existingList.addAll(newList.drop(1))
                                    } else {
                                        existingList.addAll(newList)
                                    }

                                    updateState {
                                        it.copy(
                                            myActivity = uiState.myActivity.copy(
                                                certificationsGroupedByTodoCompletedAt = existingList,
                                                pageInfo = myActivity.pageInfo
                                            )
                                        )
                                    }
                                }

                                if (myActivity.certificationsGroupedByGroupCreatedAt.isNotEmpty()) {
                                    val newList = myActivity.certificationsGroupedByGroupCreatedAt
                                    val existingList = uiState.myActivity.certificationsGroupedByGroupCreatedAt.toMutableList()

                                    if (existingList.isNotEmpty() && newList.isNotEmpty() &&
                                        existingList.last().groupName == newList.first().groupName
                                    ) {
                                        val lastItem = existingList.last()
                                        val updatedCertifications = lastItem.certificationInfo + newList.first().certificationInfo
                                        existingList[existingList.size - 1] = lastItem.copy(certificationInfo = updatedCertifications)

                                        existingList.addAll(newList.drop(1))
                                    } else {
                                        existingList.addAll(newList)
                                    }

                                    updateState {
                                        it.copy(
                                            myActivity = uiState.myActivity.copy(
                                                certificationsGroupedByGroupCreatedAt = existingList,
                                                pageInfo = myActivity.pageInfo
                                            )
                                        )
                                    }
                                }
                            }.onFailure {

                            }
                        }.invokeOnCompletion {
                            updateState { it.copy(isLoading = false) }
                        }
                    }

                    is CertificationListUiEvent.Callback.OnSwipeLeft -> {
                        if (uiState.selectedItemIndex < uiState.detailedCertifications.lastIndex) {
                            updateState { it.copy(selectedItemIndex = it.selectedItemIndex + 1) }
                        }
                    }

                    is CertificationListUiEvent.Callback.OnSwipeRight -> {
                        if (uiState.selectedItemIndex > 0) {
                            updateState { it.copy(selectedItemIndex = it.selectedItemIndex - 1) }
                        }
                    }
                }
            }
        }
    }

    private fun loadNewMyActivityData(
        sortBy: String,
        status: String? = null
    ) {
        updateState { it.copy(isLoading = true) }

        viewModelScope.launch {
            getMyActivity(
                sortBy = sortBy,
                status = status,
                page = 0
            ).onSuccess { myActivity ->
                updateState { it.copy(myActivity = myActivity) }
            }.onFailure {
                // handle exception
            }
        }.invokeOnCompletion {
            updateState { it.copy(isLoading = false) }
        }
    }

    private fun chipToStatus(chip: Chip?): String? = when (chip) {
        Chip.ReviewPending -> STATUS_REVIEW_PENDING
        Chip.Approve -> STATUS_APPROVE
        Chip.Reject -> STATUS_REJECT
        else -> null
    }
}