package site.dogether.presentation.utils

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
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
import site.dogether.presentation.composables.node.throttledClickable
import site.dogether.presentation.screen.home.state.AnchoredBottomSheetState

@Preview(
    showBackground = true,
    backgroundColor = 0xFF101010
)
annotation class ScreenPreview

val LocalNavHostController = staticCompositionLocalOf<NavHostController> {
    error("NavHostController not provided")
}

val LocalDeeplinkInfo = staticCompositionLocalOf<String?> {
    null // 기본값은 null (딥링크가 없을 때)
}

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState not provided")
}

@Composable
inline fun <reified Effect : UiEffect> BaseViewModel<*>.CollectEffect(crossinline onCollected: (Effect) -> Unit) {
    val navHostController = LocalNavHostController.current
    val snackbarHostState = LocalSnackbarHostState.current
    val errorCallbackManager = LocalErrorCallbackManager.current
    val scope = rememberCoroutineScope()

    collectSideEffect { effect ->
        when (effect) {
            is UiEffect.NavigateToPreviousScreen -> navHostController.popBackStack()

            is UiEffect.NavigateToErrorWithCallback -> {
                // 콜백 저장 후 에러 화면으로 이동
                errorCallbackManager.setCallbacks(
                    onPositive = effect.onPositive,
                    onNegative = effect.onNegative
                )
                navHostController.navigate("${Screen.ERROR}/${effect.error.name}")
            }

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

            is UiEffect.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = effect.text,
                        duration = SnackbarDuration.Short
                    )
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

fun Modifier.conditionedThrottledClickable(
    condition: Boolean,
    onClick: () -> Unit,
): Modifier = if (condition) this.then(Modifier.throttledClickable { onClick() }) else this

@Composable
fun Float.toDp() = with(LocalDensity.current) { this@toDp.toDp() }

@Composable
fun displayHeightRatio(ratio: Float): Dp =
    (LocalWindowInfo.current.containerSize.height * ratio).toDp()

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
    state: AnchoredBottomSheetState,
    scope: CoroutineScope,
): Modifier = this.then(
    Modifier.pointerInput(state) {
        detectVerticalDragGestures(
            onVerticalDrag = { change, dragAmount ->
                change.consume()
                state.dispatchRawDelta(dragAmount)
            },
            onDragEnd = {
                val current = state.sheetOffsetY
                val upper = state.upperAnchorY
                val lower = state.lowerAnchorY
                val midPoint = (upper + lower) / 2

                scope.launch {
                    if (current < midPoint) {
                        state.animateToUpper()
                    } else {
                        state.animateToLower()
                    }
                }
            }
        )
    }
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