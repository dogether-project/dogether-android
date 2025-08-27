package site.dogether.presentation.base

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

abstract class BaseViewModel<State : Any, Event, Effect : Any>(
    initialState: State,
) : ContainerHost<State, Effect>, ViewModel() {

    override val container: Container<State, Effect> = container(initialState)

    protected val uiState: State
        get() = container.stateFlow.value

    abstract fun onEvent(event: Event)

    protected fun updateState(
        reducer: (State) -> State,
    ) {
        intent {
            reduce { reducer(state) }
        }
    }

    protected fun postEffect(effect: Effect) {
        intent { postSideEffect(effect) }
    }
}