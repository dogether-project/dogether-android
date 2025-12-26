package site.dogether.presentation.screen.home.state

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Velocity

@Stable
class AnchoredBottomSheetState {
    var statusBarHeight by mutableIntStateOf(0)
    var frameHeight by mutableIntStateOf(0)
    var upperAnchorY by mutableIntStateOf(0)
    var lowerAnchorY by mutableIntStateOf(0)

    private var _sheetOffsetY by mutableFloatStateOf(0f)
    val sheetOffsetY: Float get() = _sheetOffsetY

    val expandingProgress: Float
        get() = if (lowerAnchorY == upperAnchorY) 0f else {
            ((_sheetOffsetY - upperAnchorY) / (lowerAnchorY - upperAnchorY)).coerceIn(0f, 1f)
        }

    fun snapToLowerAnchorIfNeeded() {
        if (lowerAnchorY != 0 && _sheetOffsetY == 0f) {
            _sheetOffsetY = lowerAnchorY.toFloat()
        }
    }

    fun dispatchRawDelta(delta: Float): Float {
        val current = _sheetOffsetY
        val newOffset = current + delta
        val clamped = newOffset.coerceIn(upperAnchorY.toFloat(), lowerAnchorY.toFloat())
        _sheetOffsetY = clamped
        return clamped - current
    }

    suspend fun animateToUpper(durationMillis: Int = 200) {
        animateTo(upperAnchorY.toFloat(), durationMillis)
    }

    suspend fun animateToLower(durationMillis: Int = 200) {
        animateTo(lowerAnchorY.toFloat(), durationMillis)
    }

    private suspend fun animateTo(targetY: Float, durationMillis: Int) {
        Animatable(_sheetOffsetY).animateTo(
            targetValue = targetY,
            animationSpec = tween(durationMillis)
        ) {
            _sheetOffsetY = this.value
        }
    }

    fun isExpandable(available: Offset): Boolean = available.y < 0f && _sheetOffsetY > upperAnchorY

    fun isExpandable(available: Velocity): Boolean = available.y < 0f && _sheetOffsetY > upperAnchorY

    fun isCollapsable(available: Offset): Boolean = available.y > 0f && _sheetOffsetY < lowerAnchorY

    fun isCollapsable(available: Velocity): Boolean = available.y > 0f && _sheetOffsetY < lowerAnchorY
}