package site.dogether.presentation.screen.home.state

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

@Stable
class AnchoredBottomSheetState {
    var statusBarHeight by mutableIntStateOf(0)
    var frameHeight by mutableIntStateOf(0)
    var upperAnchorY by mutableIntStateOf(0)
    var lowerAnchorY by mutableIntStateOf(0)

    val sheetOffsetY = Animatable(0f)

    val expandingProgress: Float
        get() = ((sheetOffsetY.value - upperAnchorY) / (lowerAnchorY - upperAnchorY)).coerceIn(0f, 1f)

    suspend fun snapToLowerAnchorIfNeeded() {
        if (lowerAnchorY != 0 && sheetOffsetY.value == 0f) {
            sheetOffsetY.snapTo(lowerAnchorY.toFloat())
        }
    }

    suspend fun setOffset(newValue: Float) {
        val clamped = newValue.coerceIn(upperAnchorY.toFloat(), lowerAnchorY.toFloat())
        sheetOffsetY.snapTo(clamped)
    }

    suspend fun animateToUpper(durationMillis: Int = 200) {
        sheetOffsetY.animateTo(upperAnchorY.toFloat(), animationSpec = tween(durationMillis))
    }

    suspend fun animateToLower(durationMillis: Int = 200) {
        sheetOffsetY.animateTo(lowerAnchorY.toFloat(), animationSpec = tween(durationMillis))
    }
}