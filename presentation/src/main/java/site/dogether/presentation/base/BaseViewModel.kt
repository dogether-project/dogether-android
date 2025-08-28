package site.dogether.presentation.base

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

open class BaseViewModel<State : Any>(
    initialState: State,
) : ContainerHost<State, UiEffect>, ViewModel() {

    override val container: Container<State, UiEffect> = container(initialState)

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

    protected fun updateState(
        reducer: (State) -> State,
    ) {
        intent { reduce { reducer(state) } }
    }

    protected fun postEffect(effect: UiEffect) {
        intent { postSideEffect(effect) }
    }
}