package site.dogether.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import site.dogether.presentation.utils.DayChangeTracker
import java.time.LocalDate

open class BaseViewModel<State : Any>(
    initialState: State,
) : ContainerHost<State, UiEffect>, ViewModel(), KoinComponent {

    private val dayChangeTracker: DayChangeTracker by inject()

    override val container: Container<State, UiEffect> = container(initialState)

    init {
        viewModelScope.launch {
            dayChangeTracker.dayFlow
                .drop(1)
                .collect { date ->
                    onDateChanged(date)
                }
        }
    }

    protected val uiState: State
        get() = container.stateFlow.value

    open fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Click -> {
                when (event) {
                    is UiEvent.Click.OnClickBack -> {
                        postEffect(UiEffect.NavigateToPreviousScreen)
                    }
                }
            }
        }
    }

    open fun onDateChanged(date: LocalDate) {
        // 하위 클래스에서 오버라이드하여 날짜 변경 시 동작 구현
    }

    protected fun updateState(
        reducer: (State) -> State,
    ) {
        intent { reduce { reducer(state) } }
    }

    protected fun postEffect(effect: UiEffect) {
        intent { postSideEffect(effect) }
    }

    protected fun showToast(message: String) {
        postEffect(UiEffect.ShowToast(message))
    }
}