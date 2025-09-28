package site.dogether.presentation.screen.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import site.dogether.common.HoursPerDay
import site.dogether.common.MinutesPerHour
import site.dogether.common.SecondsPerMinute
import site.dogether.domain.model.group.Group
import site.dogether.domain.model.group.Group.Companion.STATUS_D_DAY
import site.dogether.domain.model.group.Group.Companion.STATUS_FINISHED
import site.dogether.domain.model.todo.Todo
import site.dogether.domain.model.todo.Todo.Companion.STATUS_APPROVE
import site.dogether.domain.model.todo.Todo.Companion.STATUS_REJECT
import site.dogether.domain.model.todo.Todo.Companion.STATUS_REVIEW_PENDING
import site.dogether.domain.use_case.group.GetJoiningGroupsUseCase
import site.dogether.domain.use_case.group.StoreLastSelectedGroupIdUseCase
import site.dogether.domain.use_case.todo.GetMyTodoSpecificDateUseCase
import site.dogether.presentation.R
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.home.model.Chip
import site.dogether.presentation.screen.home.state.TooltipUiState
import site.dogether.presentation.utils.DATE_FORMAT_FULL_YEAR
import site.dogether.presentation.utils.toFormattedString
import site.dogether.presentation.utils.today
import site.dogether.presentation.utils.todayWithTime
import site.dogether.presentation.utils.tomorrowMidnight
import java.time.Duration.between
import java.time.LocalDate

class HomeViewModel(
    private val defaultDispatcher: CoroutineDispatcher,
    private val getJoiningGroups: GetJoiningGroupsUseCase,
    private val storeLastSelectedGroupId: StoreLastSelectedGroupIdUseCase,
    private val getMyTodoSpecificDate: GetMyTodoSpecificDateUseCase
) : BaseViewModel<HomeUiState>(HomeUiState()) {

    private lateinit var timerJob: Job

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

                            updateState { it.copy(groups = getJoiningGroupsResult.groups) }
                            selectGroup(getJoiningGroupsResult.groups[getJoiningGroupsResult.lastSelectedGroupIndex])
                        }

                        postEffect(HomeUiEffect.CheckNotificationPermission)
                    }
                }
            }

            is HomeUiEvent.Click -> {
                when (event) {
                    is HomeUiEvent.Click.OnClickChip -> {
                        updateState { it.copy(selectedChip = event.chip) }
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
                        viewModelScope.launch {
                            selectGroup(group = event.group)
                        }
                    }

                    is HomeUiEvent.Click.OnClickAddGroup -> {
                        updateState { it.copy(isSelectGroupBottomSheetShowing = false) }
                        postEffect(UiEffect.NavigateTo(Screen.PARTICIPATION_METHOD))
                    }

                    is HomeUiEvent.Click.OnClickPrevDay -> {
                        viewModelScope.launch {
                            val date = uiState.selectedDate.minusDays(1)
                            val getMyTodoSpecificDateResult = getMyTodoSpecificDate(
                                groupId = uiState.selectedGroup.id,
                                date = date.toFormattedString(DATE_FORMAT_FULL_YEAR)
                            ).getOrElse {
                                // handle exception
                                return@launch
                            }.todos

                            updateState {
                                it.copy(
                                    selectedDate = date,
                                    todoList = getMyTodoSpecificDateResult
                                )
                            }
                        }
                    }

                    is HomeUiEvent.Click.OnClickNextDay -> {
                        viewModelScope.launch {
                            val date = uiState.selectedDate.plusDays(1)
                            val getMyTodoSpecificDateResult = getMyTodoSpecificDate(
                                groupId = uiState.selectedGroup.id,
                                date = date.toFormattedString(DATE_FORMAT_FULL_YEAR)
                            ).getOrElse {
                                // handle exception
                                return@launch
                            }.todos

                            updateState {
                                it.copy(
                                    selectedDate = date,
                                    todoList = getMyTodoSpecificDateResult
                                )
                            }
                        }
                    }

                    is HomeUiEvent.Click.OnClickDismissTooltip -> {
                        updateState { it.copy(tooltipUiState = it.tooltipUiState.copy(isShowing = false)) }
                    }

                    HomeUiEvent.Click.OnClickCreateTodo -> {
                        postEffect(HomeUiEffect.NavigateToCreateTodo)
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

        timerJob = viewModelScope.launch(defaultDispatcher) {
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

    private suspend fun selectGroup(group: Group) {
        if (uiState.selectedGroup.progressDay == 0) {
            launchTomorrowTimer()
        } else {
            if (::timerJob.isInitialized) {
                timerJob.cancel()
            }
        }

        val getMyTodoSpecificDateResult = getMyTodoSpecificDate(
            groupId = group.id,
            date = today.toFormattedString(DATE_FORMAT_FULL_YEAR)
        ).getOrElse {
            // handle exception
            return
        }.todos

        storeLastSelectedGroupId(group.id).getOrElse {
            // handle exception
        }

        updateState {
            it.copy(
                selectedGroup = group,
                todoList = getMyTodoSpecificDateResult,
                selectedDate = today,
                selectedChip = Chip.All,
                tooltipUiState = TooltipUiState(
                    isShowing = group.status == STATUS_D_DAY || group.status == STATUS_FINISHED,
                    stringId = when (group.status) {
                        STATUS_D_DAY -> R.string.tooltip_group_d_day
                        STATUS_FINISHED -> R.string.tooltip_group_finished
                        else -> null
                    }
                ),
                isSelectGroupBottomSheetShowing = false
            )
        }
    }
}