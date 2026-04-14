package site.dogether.presentation.screen.my_page.screen.certification_list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import site.dogether.domain.model.todo.Todo.Companion.STATUS_APPROVE
import site.dogether.domain.model.todo.Todo.Companion.STATUS_REJECT
import site.dogether.domain.model.todo.Todo.Companion.STATUS_REVIEW_PENDING
import site.dogether.domain.use_case.todo.GetMyActivityUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error
import site.dogether.presentation.screen.my_page.screen.certification_list.model.Chip
import site.dogether.presentation.screen.my_page.screen.certification_list.model.SortingMethod

class CertificationListViewModel(
    private val getMyActivity: GetMyActivityUseCase
) : BaseViewModel<CertificationListUiState>(CertificationListUiState()) {

    private data class MyActivityQuery(
        val sortBy: String,
        val status: String?
    )

    private val queryFlow = MutableStateFlow(
        MyActivityQuery(
            sortBy = uiState.selectedSortingMethod.serverString,
            status = null
        )
    )

    private val loadNextPageFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private var isPagingRequestInProgress = false

    init {
        collectInitialMyActivity()
        collectNextPageRequest()
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
                        requestMyActivityRefresh(
                            sortBy = event.sortingMethod.serverString,
                            status = chipToStatus(uiState.selectedChip)
                        )
                    }

                    is CertificationListUiEvent.Click.OnClickChip -> {
                        updateState { it.copy(selectedChip = event.chip) }
                        requestMyActivityRefresh(
                            sortBy = uiState.selectedSortingMethod.serverString,
                            status = chipToStatus(event.chip)
                        )
                    }

                    is CertificationListUiEvent.Click.OnClickCertificationInfo -> {
                        updateState {
                            it.copy(
                                isDetailMode = true,
                                detailedCertifications = event.detailedCertifications,
                                selectedItemIndex = event.index,
                                detailTitle = if (it.selectedSortingMethod == SortingMethod.DescendTodoCompleted) event.date else event.groupName
                            )
                        }
                    }

                    is CertificationListUiEvent.Click.OnClickBackWhenDetailMode -> {
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
                        loadNextPageFlow.tryEmit(Unit)
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

    private fun collectInitialMyActivity() {
        viewModelScope.launch {
            queryFlow.collectLatest { query ->
                loadNewMyActivityData(query)
            }
        }
    }

    private fun collectNextPageRequest() {
        viewModelScope.launch {
            loadNextPageFlow.collect {
                loadNextPage()
            }
        }
    }

    private fun requestMyActivityRefresh(
        sortBy: String,
        status: String?
    ) {
        queryFlow.update {
            MyActivityQuery(
                sortBy = sortBy,
                status = status
            )
        }
    }

    private suspend fun loadNewMyActivityData(query: MyActivityQuery) {
        updateState { it.copy(isLoading = true) }

        getMyActivity(
            sortBy = query.sortBy,
            status = query.status,
            page = 0
        ).onSuccess { myActivity ->
            updateState { it.copy(myActivity = myActivity) }
        }.onFailure {
            postEffect(
                UiEffect.NavigateToErrorWithCallback(
                    error = Error.LoadData,
                    onPositive = {
                        requestMyActivityRefresh(
                            sortBy = query.sortBy,
                            status = query.status
                        )
                    }
                )
            )
        }

        updateState { it.copy(isLoading = false) }
    }

    private suspend fun loadNextPage() {
        if (uiState.isLoading || !uiState.myActivity.pageInfo.hasNext || isPagingRequestInProgress) return

        isPagingRequestInProgress = true
        updateState { it.copy(isLoading = true) }

        val query = queryFlow.value
        getMyActivity(
            sortBy = query.sortBy,
            status = query.status,
            page = uiState.myActivity.pageInfo.recentPageNumber + 1
        ).onSuccess { myActivity ->
            updateState {
                it.copy(
                    myActivity = it.myActivity.copy(
                        certificationsGroupedByTodoCompletedAt = mergeCertificationGroups(
                            current = it.myActivity.certificationsGroupedByTodoCompletedAt,
                            incoming = myActivity.certificationsGroupedByTodoCompletedAt
                        ),
                        certificationsGroupedByGroupCreatedAt = mergeCertificationGroups(
                            current = it.myActivity.certificationsGroupedByGroupCreatedAt,
                            incoming = myActivity.certificationsGroupedByGroupCreatedAt,
                            keySelector = { group -> group.groupName }
                        ),
                        pageInfo = myActivity.pageInfo
                    )
                )
            }
        }.onFailure { error ->
            postEffect(UiEffect.ShowToast(error.message ?: "인증 목록을 불러오는데 실패했습니다"))
        }

        isPagingRequestInProgress = false
        updateState { it.copy(isLoading = false) }
    }

    private fun mergeCertificationGroups(
        current: List<site.dogether.domain.model.todo.GroupedCertification>,
        incoming: List<site.dogether.domain.model.todo.GroupedCertification>,
        keySelector: (site.dogether.domain.model.todo.GroupedCertification) -> String = { group -> group.createdAt }
    ): List<site.dogether.domain.model.todo.GroupedCertification> {
        if (incoming.isEmpty()) return current
        if (current.isEmpty()) return incoming

        val merged = current.toMutableList()
        if (keySelector(merged.last()) == keySelector(incoming.first())) {
            val lastItem = merged.last()
            merged[merged.lastIndex] = lastItem.copy(
                certificationInfo = lastItem.certificationInfo + incoming.first().certificationInfo
            )
            merged.addAll(incoming.drop(1))
            return merged
        }

        merged.addAll(incoming)
        return merged
    }

    private fun chipToStatus(chip: Chip?): String? = when (chip) {
        Chip.ReviewPending -> STATUS_REVIEW_PENDING
        Chip.Approve -> STATUS_APPROVE
        Chip.Reject -> STATUS_REJECT
        else -> null
    }
}