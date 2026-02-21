package site.dogether.presentation.screen.home

import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import site.dogether.common.HoursPerDay
import site.dogether.common.MinutesPerHour
import site.dogether.common.SecondsPerMinute
import site.dogether.common.utils.DateTimeUtils.DATE_FORMAT_FULL_YEAR
import site.dogether.common.utils.DateTimeUtils.DATE_FORMAT_FULL_YEAR_DASHED
import site.dogether.common.utils.DateTimeUtils.toFormattedString
import site.dogether.common.utils.DateTimeUtils.today
import site.dogether.domain.model.group.Group
import site.dogether.domain.model.group.Group.Companion.STATUS_D_DAY
import site.dogether.domain.model.group.Group.Companion.STATUS_FINISHED
import site.dogether.domain.use_case.group.GetJoiningGroupsUseCase
import site.dogether.domain.use_case.group.StoreLastSelectedGroupIdUseCase
import site.dogether.domain.use_case.todo.GetMyTodoSpecificDateUseCase
import site.dogether.presentation.R
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error
import site.dogether.presentation.screen.home.model.Chip
import site.dogether.presentation.screen.home.state.TooltipUiState
import java.time.Duration.between
import java.time.LocalDate
import java.time.LocalDateTime

class HomeViewModel(
    private val defaultDispatcher: CoroutineDispatcher,
    private val getJoiningGroups: GetJoiningGroupsUseCase,
    private val storeLastSelectedGroupId: StoreLastSelectedGroupIdUseCase,
    private val getMyTodoSpecificDate: GetMyTodoSpecificDateUseCase,
) : BaseViewModel<HomeUiState>(HomeUiState()) {

    private lateinit var timerJob: Job

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is HomeUiEvent.Lifecycle -> {
                when (event) {
                    is HomeUiEvent.Lifecycle.OnFirstComposition -> {
                        loadInitialData()
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
                        loadTodosForDate(uiState.selectedDate.minusDays(1))
                    }

                    is HomeUiEvent.Click.OnClickNextDay -> {
                        loadTodosForDate(uiState.selectedDate.plusDays(1))
                    }

                    is HomeUiEvent.Click.OnClickDismissTooltip -> {
                        updateState { it.copy(tooltipUiState = it.tooltipUiState.copy(isShowing = false)) }
                    }

                    HomeUiEvent.Click.OnClickCreateTodo -> {
                        postEffect(HomeUiEffect.NavigateToCreateTodo)
                    }

                    is HomeUiEvent.Click.OnClickCertificateTodo -> {
                        postEffect(
                            HomeUiEffect.NavigateToCertificateTodo(
                                todoId = event.todoId,
                                todoTitle = event.todoTitle
                            )
                        )
                    }

                    is HomeUiEvent.Click.OnClickTodo -> {
                        postEffect(
                            HomeUiEffect.NavigateToMyCertInfo(
                                groupId = uiState.selectedGroup.id,
                                todoIndex = event.todoIndex,
                                date = uiState.selectedDate.toFormattedString(DATE_FORMAT_FULL_YEAR_DASHED)
                            )
                        )
                    }

                    is HomeUiEvent.Click.OnClickMyPage -> {
                        postEffect(HomeUiEffect.NavigateToMyPage)
                    }

                    is HomeUiEvent.Click.OnClickRanking -> {
                        postEffect(HomeUiEffect.NavigateToRanking(uiState.selectedGroup.id))
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
        if (::timerJob.isInitialized) {
            timerJob.cancel()
        }

        val totalSecondsInDay = HoursPerDay * MinutesPerHour * SecondsPerMinute

        timerJob = viewModelScope.launch(defaultDispatcher) {
            while (true) {
                val now = LocalDateTime.now()
                val midnight = LocalDate.now().plusDays(1).atStartOfDay()
                val remainingSeconds = between(now, midnight).seconds

                if (remainingSeconds <= 0) {
                    updateState { it.copy(timerProgress = 0f, timerText = "00:00:00") }
                    break
                }

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

                delay(1000L)
            }
        }
    }

    private fun loadInitialData() {
        updateState { it.copy(isLoading = true) }

        viewModelScope.launch {
            val getJoiningGroupsResult = getJoiningGroups().getOrElse {
                updateState { it.copy(isLoading = false) }
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { loadInitialData() }
                    )
                )
                return@launch
            }

            updateState { it.copy(groups = getJoiningGroupsResult.groups.toImmutableList()) }
            selectGroup(getJoiningGroupsResult.groups[getJoiningGroupsResult.lastSelectedGroupIndex])
        }.invokeOnCompletion {
            updateState { it.copy(isLoading = false) }
        }
    }

    private fun loadTodosForDate(date: java.time.LocalDate) {
        viewModelScope.launch {
            val getMyTodoSpecificDateResult = getMyTodoSpecificDate(
                groupId = uiState.selectedGroup.id,
                date = date.toFormattedString(DATE_FORMAT_FULL_YEAR)
            ).getOrElse {
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { loadTodosForDate(date) }
                    )
                )
                return@launch
            }.todos

            updateState {
                it.copy(
                    selectedDate = date,
                    todoList = getMyTodoSpecificDateResult.toImmutableList()
                )
            }
        }
    }

    private suspend fun selectGroup(group: Group) {
        if (::timerJob.isInitialized) {
            timerJob.cancel()
        }

        if (group.progressDay == 0) {
            launchTomorrowTimer()
        }

        val getMyTodoSpecificDateResult = getMyTodoSpecificDate(
            groupId = group.id,
            date = today.toFormattedString(DATE_FORMAT_FULL_YEAR)
        ).getOrElse {
            postEffect(
                UiEffect.NavigateToErrorWithCallback(
                    error = Error.LoadData,
                    onPositive = {
                        viewModelScope.launch { selectGroup(group) }
                    }
                )
            )
            return
        }.todos

        storeLastSelectedGroupId(group.id).getOrElse {
            // 로컬 저장 실패는 무시
        }

        updateState {
            it.copy(
                selectedGroup = group,
                todoList = getMyTodoSpecificDateResult.toImmutableList(),
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