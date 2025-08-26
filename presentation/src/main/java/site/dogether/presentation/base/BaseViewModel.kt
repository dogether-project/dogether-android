package site.dogether.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

abstract class BaseViewModel<State : Any, Event, Effect : Any>(
    initialState: State,
) : ContainerHost<State, Effect>, ViewModel() {

    override val container: Container<State, Effect> = container(initialState)

    private val events = Channel<Event>(
        capacity = BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        events.receiveAsFlow()
            .debounce(200L)
            .onEach { handleEvent(it) }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: Event) {
        events.trySend(event)
    }

    protected abstract fun handleEvent(event: Event)

    protected fun updateState(
        condition: ((State) -> Boolean)? = null,
        reducer: (State) -> State,
    ) {
        intent {
            reduce {
                condition?.let {
                    if (condition(state)) {
                        reducer(state)
                    } else {
                        state
                    }
                } ?: run { reducer(state) }
            }
        }
    }

    protected fun postEffect(effect: Effect) {
        intent { postSideEffect(effect) }
    }
}