package site.dogether.presentation.screen.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import site.dogether.common.HoursPerDay
import site.dogether.common.MinutesPerHour
import site.dogether.common.SecondsPerMinute
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.model.Todo.Companion.STATUS_APPROVE
import site.dogether.presentation.model.Todo.Companion.STATUS_REJECT
import site.dogether.presentation.model.Todo.Companion.STATUS_REVIEW_PENDING
import site.dogether.presentation.screen.home.model.Chip
import site.dogether.presentation.utils.today
import site.dogether.presentation.utils.tomorrowMidnight
import java.time.Duration.between

class HomeViewModel(
    private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiEffect>(HomeUiState()) {

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
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
                        updateState { it.copy(isSelectGroupBottomSheetExpanded = true) }
                    }
                }
            }

            is HomeUiEvent.Callback -> {
                when (event) {
                    is HomeUiEvent.Callback.OnSelectGroupBottomSheetDismissRequested -> {
                        updateState { it.copy(isSelectGroupBottomSheetExpanded = false) }
                    }
                }
            }
        }
    }

    private fun launchTomorrowTimer() {
        val totalSecondsInDay = HoursPerDay * MinutesPerHour * SecondsPerMinute
        var remainingSeconds = between(today, tomorrowMidnight).seconds

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