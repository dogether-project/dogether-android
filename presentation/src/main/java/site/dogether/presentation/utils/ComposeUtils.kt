package site.dogether.presentation.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.util.lerp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectSideEffect
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect

@Preview(
    showBackground = true,
    backgroundColor = 0xFF101010
)
annotation class ScreenPreview

val LocalNavHostController = staticCompositionLocalOf<NavHostController> {
    error("NavHostController not provided")
}

@Composable
inline fun <reified Effect : UiEffect> BaseViewModel<*>.CollectEffect(crossinline onCollected: (Effect) -> Unit) {
    val navHostController = LocalNavHostController.current
    collectSideEffect { effect ->
        when (effect) {
            is UiEffect.NavigateToPreviousScreen -> navHostController.popBackStack()

            is UiEffect.NavigateToError -> navHostController.navigate(Screen.ERROR)

            is UiEffect.NavigateTo -> {
                navHostController.navigate(effect.screen) {
                    if (effect.clearBackStack) {
                        popUpTo(navHostController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }

            is Effect -> onCollected(effect)
            else -> Unit
        }
    }
}

@Composable
fun Dp.toSp(): TextUnit = with(LocalDensity.current) { this@toSp.toSp() }

@Composable
fun Dp.toPx(): Float = with(LocalDensity.current) { this@toPx.toPx() }

fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier = this.clickable(
    interactionSource = MutableInteractionSource(),
    indication = null
) {
    onClick()
}

fun Modifier.conditionedClickable(
    condition: Boolean,
    onClick: () -> Unit,
): Modifier = if (condition) this.then(Modifier.clickable { onClick() }) else this

fun Modifier.conditionedClickableWithoutRipple(
    condition: Boolean,
    onClick: () -> Unit,
): Modifier = if (condition) this.then(Modifier.clickableWithoutRipple { onClick() }) else this


fun Modifier.intervaledClickable(
    interval: Long = 500L,
    onClick: () -> Unit,
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > interval) {
            lastClickTime = currentTime
            onClick()
        }
    }
}

fun Modifier.intervaledClickableWithoutRipple(
    interval: Long = 500L,
    onClick: () -> Unit,
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > interval) {
            lastClickTime = currentTime
            onClick()
        }
    }
}

@Composable
fun Float.toDp() = with(LocalDensity.current) { this@toDp.toDp() }

@Composable
fun displayHeightRatio(ratio: Float): Dp = (LocalWindowInfo.current.containerSize.height * ratio).toDp()

fun Color.alpha(alpha: Int) = this.copy(alpha = alpha / 100f)

@Composable
fun LifecycleEvent(
    targetEvent: Lifecycle.Event,
    onEvent: (Lifecycle.Event) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == targetEvent) {
                onEvent(event)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun Modifier.hideKeyboardOnTap(): Modifier {
    val focusManager = LocalFocusManager.current
    return this.then(Modifier.pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) })
}

fun Modifier.bottomSheetSnappable(
    sheetOffsetY: Animatable<Float, AnimationVector1D>,
    upperLimit: Int,
    lowerLimit: Int,
    scope: CoroutineScope,
): Modifier = this.then(
    Modifier.pointerInput(
        key1 = upperLimit,
        key2 = lowerLimit,
        block = {
            detectVerticalDragGestures(
                onVerticalDrag = { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        val newOffset = (sheetOffsetY.value + dragAmount).coerceIn(upperLimit.toFloat(), lowerLimit.toFloat())
                        sheetOffsetY.snapTo(newOffset)
                    }
                },
                onDragEnd = {
                    val current = sheetOffsetY.value
                    val nearest = if ((current - upperLimit) < (lowerLimit - current)) upperLimit.toFloat() else lowerLimit.toFloat()
                    scope.launch {
                        sheetOffsetY.animateTo(
                            targetValue = nearest,
                            animationSpec = tween(durationMillis = 200)
                        )
                    }
                }
            )
        }
    )
)

fun Modifier.alphaByProgress(progress: Float): Modifier = this.then(
    Modifier.alpha(
        lerp(
            start = 0f,
            stop = 1f,
            fraction = progress.coerceIn(
                minimumValue = 0f,
                maximumValue = 1f
            )
        )
    )
)

suspend fun LazyListState.animateScrollToItemCenteredFixedWidth(
    index: Int,
    itemWidthPx: Int,
) {
    val viewportWidth = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
    val offset = viewportWidth / 2 - itemWidthPx / 2
    animateScrollToItem(index, scrollOffset = -offset)
}