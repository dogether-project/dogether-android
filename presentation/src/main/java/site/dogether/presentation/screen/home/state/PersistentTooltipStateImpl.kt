package site.dogether.presentation.screen.home.state

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.MutatePriority
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Stable

@OptIn(ExperimentalMaterial3Api::class)
@Stable
class PersistentTooltipStateImpl(
    initialIsVisible: Boolean = false,
) : TooltipState {

    override val transition = MutableTransitionState(initialIsVisible)

    override val isPersistent: Boolean = true

    override val isVisible: Boolean
        get() = transition.currentState || transition.targetState

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This method will not work properly. use forceDismiss() instead."
    )
    override fun dismiss() = Unit

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This method will not work properly. use forceDismiss() instead."
    )
    override fun onDispose() = Unit

    override suspend fun show(mutatePriority: MutatePriority) {
        transition.targetState = true
    }

    suspend fun forceShow() = show(MutatePriority.Default)

    fun forceDismiss() {
        transition.targetState = false
    }
}