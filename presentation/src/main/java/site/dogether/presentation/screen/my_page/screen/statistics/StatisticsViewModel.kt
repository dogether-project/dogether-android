package site.dogether.presentation.screen.my_page.screen.statistics

import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.group.GetJoiningGroupsUseCase
import site.dogether.domain.use_case.user.GetGroupStatisticsUseCase
import site.dogether.domain.model.user.GroupCertificationStatistics
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error

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

                    is StatisticsUiEvent.Click.OnClickCreateGroup -> {
                        postEffect(UiEffect.NavigateTo("${Screen.PARTICIPATION_METHOD}/${false}"))
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
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            updateState { it.copy(isGroupsLoading = true) }

            getJoiningGroupsUseCase().onSuccess { joiningGroups ->
                if (joiningGroups.lastSelectedGroupIndex == -1) return@launch

                val selectedGroup = joiningGroups.groups[joiningGroups.lastSelectedGroupIndex]
                val groupId = selectedGroup.id

                updateState {
                    it.copy(
                        groups = joiningGroups.groups.toImmutableList(),
                        selectedGroup = selectedGroup
                    )
                }

                getGroupStatistics(groupId)
            }.onFailure {
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { loadInitialData() }
                    )
                )
            }
        }.invokeOnCompletion {
            updateState { it.copy(isGroupsLoading = false) }
        }
    }

    private fun getGroupStatistics(groupId: Int) {
        viewModelScope.launch {
            updateState { it.copy(isStatisticsLoading = true) }

            getGroupStatisticsUseCase(groupId).onSuccess { groupStatistics ->
                val certificationPeriods = groupStatistics.certificationPeriods
                val lastDay = certificationPeriods.lastOrNull()?.day ?: 0
                val displayCertificationPeriods = (lastDay - 3..lastDay).filter { it > 0 }.map { currentDay ->
                    certificationPeriods.find { it.day == currentDay } ?: GroupCertificationStatistics(day = currentDay)
                }.toImmutableList()

                updateState {
                    it.copy(
                        groupStatistics = groupStatistics,
                        displayCertificationPeriods = displayCertificationPeriods
                    )
                }
            }.onFailure {
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { getGroupStatistics(groupId) }
                    )
                )
            }
        }.invokeOnCompletion {
            updateState { it.copy(isStatisticsLoading = false) }
        }
    }
}