package site.dogether.presentation.screen.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import site.dogether.common.HoursPerDay
import site.dogether.common.MinutesPerHour
import site.dogether.common.SecondsPerMinute
import site.dogether.domain.use_case.group.GetJoiningGroupsUseCase
import site.dogether.domain.use_case.group.StoreLastSelectedGroupIdUseCase
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.Todo.Companion.STATUS_APPROVE
import site.dogether.presentation.model.Todo.Companion.STATUS_REJECT
import site.dogether.presentation.model.Todo.Companion.STATUS_REVIEW_PENDING
import site.dogether.presentation.screen.home.model.Chip
import site.dogether.presentation.utils.today
import site.dogether.presentation.utils.todayWithTime
import site.dogether.presentation.utils.tomorrowMidnight
import java.time.Duration.between

class HomeViewModel(
    private val defaultDispatcher: CoroutineDispatcher,
    private val getJoiningGroups: GetJoiningGroupsUseCase,
    private val storeLastSelectedGroupId: StoreLastSelectedGroupIdUseCase,
) : BaseViewModel<HomeUiState>(HomeUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is HomeUiEvent.Lifecycle -> {
                when (event) {
                    is HomeUiEvent.Lifecycle.OnFirstComposition -> {
                        viewModelScope.launch {
                            val getJoiningGroupsResult = getJoiningGroups().getOrElse {
                                // handle exception
                                return@launch
                            }

                            val selectedGroup = getJoiningGroupsResult.groups[getJoiningGroupsResult.lastSelectedGroupIndex]

                            updateState {
                                it.copy(
                                    selectedGroup = selectedGroup,
                                    groups = getJoiningGroupsResult.groups,
                                    selectedDate = today,
                                )
                            }

                            if (uiState.selectedGroup.progressDay == 0) {
                                launchTomorrowTimer()
                            }

                            storeLastSelectedGroupId(selectedGroup.id).getOrElse {
                                // handle exception
                            }
                        }

                        postEffect(HomeUiEffect.CheckNotificationPermission)
                    }
                }
            }

            is HomeUiEvent.Click -> {
                when (event) {
                    is HomeUiEvent.Click.OnClickChip -> {
                        updateState {
                            it.copy(
                                selectedChip = event.chip,
                                filteredTodoList = it.todoList.filter { todo ->
                                    when (event.chip) {
                                        Chip.All -> true
                                        Chip.Approve -> todo.status == STATUS_APPROVE
                                        Chip.Reject -> todo.status == STATUS_REJECT
                                        Chip.ReviewPending -> todo.status == STATUS_REVIEW_PENDING
                                    }
                                }
                            )
                        }
                    }

                    is HomeUiEvent.Click.OnClickSelectGroup -> {
                        updateState { it.copy(isSelectGroupBottomSheetShowing = true) }
                    }

                    is HomeUiEvent.Click.OnClickPermissionDialogNegative -> {
                        updateState { it.copy(permissionDialogState = it.permissionDialogState.copy(isShowing = false)) }
                    }

                    is HomeUiEvent.Click.OnClickPermissionDialogPositive -> {
                        updateState { it.copy(permissionDialogState = it.permissionDialogState.copy(isShowing = false)) }
                        postEffect(HomeUiEffect.NavigateToNotificationSettings)
                    }

                    is HomeUiEvent.Click.OnClickGroup -> {
                        updateState {
                            it.copy(
                                selectedGroup = event.group,
                                isSelectGroupBottomSheetShowing = false
                            )
                        }
                    }

                    is HomeUiEvent.Click.OnClickAddGroup -> {
                        updateState { it.copy(isSelectGroupBottomSheetShowing = false) }
                        postEffect(UiEffect.NavigateTo(Screen.PARTICIPATION_METHOD))
                    }

                    is HomeUiEvent.Click.OnClickPrevDay -> {
                        updateState { it.copy(selectedDate = uiState.selectedDate.minusDays(1)) }
                    }

                    is HomeUiEvent.Click.OnClickNextDay -> {
                        updateState { it.copy(selectedDate = uiState.selectedDate.plusDays(1)) }
                    }
                }
            }

            is HomeUiEvent.Callback -> {
                when (event) {
                    is HomeUiEvent.Callback.OnNotificationPermissionDenied -> {
                        updateState { it.copy(permissionDialogState = it.permissionDialogState.copy(isShowing = true)) }
                    }

                    is HomeUiEvent.Callback.OnPermissionDialogDismissRequested -> {
                        updateState { it.copy(permissionDialogState = it.permissionDialogState.copy(isShowing = false)) }
                    }

                    is HomeUiEvent.Callback.OnSelectGroupBottomSheetDismissRequested -> {
                        updateState { it.copy(isSelectGroupBottomSheetShowing = false) }
                    }
                }
            }
        }
    }

    private fun launchTomorrowTimer() {
        val totalSecondsInDay = HoursPerDay * MinutesPerHour * SecondsPerMinute
        var remainingSeconds = between(todayWithTime, tomorrowMidnight).seconds

        viewModelScope.launch(defaultDispatcher) {
            while (remainingSeconds > 0) {
                delay(1000L)
                remainingSeconds--

                val hours = remainingSeconds / (MinutesPerHour * SecondsPerMinute)
                val minutes = (remainingSeconds % (MinutesPerHour * SecondsPerMinute)) / SecondsPerMinute
                val seconds = remainingSeconds % SecondsPerMinute

                val progress = 1f - (remainingSeconds.toFloat() / totalSecondsInDay)
                val text = String.format(null, "%02d:%02d:%02d", hours, minutes, seconds)

                updateState {
                    it.copy(
                        timerProgress = progress,
                        timerText = text
                    )
                }
            }
        }
    }
}