package site.dogether.presentation.screen.my_page.screen.statistics

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.group.GetJoiningGroupsUseCase
import site.dogether.domain.use_case.user.GetGroupStatisticsUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class StatisticsViewModel(
    private val getJoiningGroupsUseCase: GetJoiningGroupsUseCase,
    private val getGroupStatisticsUseCase: GetGroupStatisticsUseCase
) : BaseViewModel<StatisticsUiState>(StatisticsUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is StatisticsUiEvent.Click -> {
                when (event) {
                    is StatisticsUiEvent.Click.OnClickSelectGroup -> {
                        updateState { it.copy(isSelectGroupBottomSheetShowing = true) }
                    }

                    is StatisticsUiEvent.Click.OnClickGroupItem -> {
                        updateState {
                            it.copy(
                                selectedGroup = event.group,
                                isSelectGroupBottomSheetShowing = false
                            )
                        }
                        getGroupStatistics(event.group.id)
                    }
                }
            }

            is StatisticsUiEvent.Callback -> {
                when (event) {
                    is StatisticsUiEvent.Callback.OnSelectGroupBottomSheetDismissRequested -> {
                        updateState { it.copy(isSelectGroupBottomSheetShowing = false) }
                    }
                }
            }
        }
    }

    init {
        updateState { it.copy(isLoading = true) }

        viewModelScope.launch {
            getJoiningGroupsUseCase().onSuccess { joiningGroups ->
                val selectedGroup = joiningGroups.groups[joiningGroups.lastSelectedGroupIndex]
                val groupId = selectedGroup.id

                updateState {
                    it.copy(
                        groups = joiningGroups.groups,
                        selectedGroup = selectedGroup
                    )
                }

                getGroupStatistics(groupId)
            }.onFailure {
                // handle exception
            }
        }.invokeOnCompletion {
            updateState { it.copy(isLoading = false) }
        }
    }

    private fun getGroupStatistics(groupId: Int) {
        updateState { it.copy(isLoading = true) }

        viewModelScope.launch {
            getGroupStatisticsUseCase(groupId).onSuccess { groupStatistics ->
                updateState { it.copy(groupStatistics = groupStatistics) }
            }.onFailure {
                // handle exception
            }
        }.invokeOnCompletion {
            updateState { it.copy(isLoading = false) }
        }
    }
}