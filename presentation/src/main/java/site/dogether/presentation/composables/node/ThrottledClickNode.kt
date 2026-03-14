package site.dogether.presentation.composables.node
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

class ThrottledClickableNode(
    var throttleTime: Long,
    var onClick: () -> Unit
) : DelegatingNode() {

    private var lastClickTime = 0L

    init {
        delegate(
            SuspendingPointerInputModifierNode {
                detectTapGestures {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime > throttleTime) {
                        lastClickTime = currentTime
                        onClick()
                    }
                }
            }
        )
    }
}

data class ThrottledClickableElement(
    val throttleTime: Long,
    val onClick: () -> Unit
) : ModifierNodeElement<ThrottledClickableNode>() {

    override fun create(): ThrottledClickableNode = ThrottledClickableNode(
        throttleTime = throttleTime,
        onClick = onClick
    )

    override fun update(node: ThrottledClickableNode) {
        node.throttleTime = throttleTime
        node.onClick = onClick
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "throttledClickable"
        properties["throttleTime"] = throttleTime
        properties["onClick"] = onClick
    }
}

fun Modifier.throttledClickable(
    throttleTime: Long = 500L,
    onClick: () -> Unit
): Modifier = this.then(
    ThrottledClickableElement(
        throttleTime = throttleTime,
        onClick = onClick
    )
)