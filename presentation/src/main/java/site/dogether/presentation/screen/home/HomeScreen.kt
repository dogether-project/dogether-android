package site.dogether.presentation.screen.home

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onLayoutRectChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBgPrimary
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorIconDisabled
import site.dogether.presentation.theme.ColorIconElevated
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextPrimary
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.theme.Head2_B
import site.dogether.presentation.theme.Small_R
import site.dogether.presentation.utils.alphaByProgress
import site.dogether.presentation.utils.bottomSheetSnappable

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    HomeScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

@Composable
private fun HomeScreenContents(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var upperAnchorY by remember { mutableIntStateOf(0) }
    var lowerAnchorY by remember { mutableIntStateOf(0) }
    val sheetOffsetY = remember { Animatable(0f) }
    val bottomSheetExpandingProgress by remember {
        derivedStateOf {
            ((sheetOffsetY.value - upperAnchorY) / (lowerAnchorY - upperAnchorY)).coerceIn(0f, 1f)
        }
    }
    var frameHeight by remember { mutableIntStateOf(0) }

    LaunchedEffect(lowerAnchorY) {
        if (lowerAnchorY != 0 && sheetOffsetY.value == 0f) {
            sheetOffsetY.snapTo(lowerAnchorY.toFloat())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onLayoutRectChanged(
                throttleMillis = 50L,
                debounceMillis = 0L
            ) { bounds -> frameHeight = bounds.height }
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            TopBar(
                start = {
                    Icon(
                        painter = painterResource(R.drawable.ic_logo_text_small),
                        tint = ColorIconDefault,
                        contentDescription = "icon_logo_text_small"
                    )
                },
                end = {
                    Icon(
                        painter = painterResource(R.drawable.ic_my),
                        tint = ColorIconDefault,
                        contentDescription = "icon_my"
                    )
                }
            )

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "DND 작심삼일 탈출러",
                            style = Head1_B.copy(lineHeightStyle = LineHeightStyle.Default),
                            color = ColorTextPrimary
                        )

                        Icon(
                            modifier = Modifier.padding(start = 4.dp),
                            painter = painterResource(R.drawable.ic_arrow_down),
                            tint = ColorIconElevated,
                            contentDescription = "icon_arrow_down"
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .onLayoutRectChanged { bounds -> upperAnchorY = bounds.positionInRoot.y - bounds.height }
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.info_group_member_limit),
                                style = Body2_R,
                                color = ColorTextSecondary
                            )

                            Text(
                                text = "6/10",
                                style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default.copy(trim = LineHeightStyle.Trim.None)),
                                color = ColorTextDefault
                            )
                        }

                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = stringResource(R.string.info_invite_code),
                                style = Body2_R,
                                color = ColorTextSecondary
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "12345678",
                                    style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default.copy(trim = LineHeightStyle.Trim.None)),
                                    color = ColorTextDefault
                                )

                                Icon(
                                    painter = painterResource(R.drawable.ic_copy),
                                    tint = ColorIconDefault,
                                    contentDescription = "icon_copy"
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = stringResource(R.string.info_end_date),
                                style = Body2_R,
                                color = ColorTextSecondary
                            )

                            Text(
                                text = "25.02.22",
                                style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default.copy(trim = LineHeightStyle.Trim.None)),
                                color = ColorTextDefault
                            )
                        }
                    }
                }

                Image(
                    modifier = Modifier
                        .alphaByProgress(bottomSheetExpandingProgress)
                        .size(100.dp),
                    painter = painterResource(R.drawable.img_dosik_main),
                    contentDescription = "image_dosik_main"
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 22.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.info_progress),
                    style = Body2_R.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextSecondary
                )

                Text(
                    text = "(n${stringResource(R.string.unit_day_passed)})",
                    style = Small_R.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextSecondary
                )

                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clip(RoundedCornerShape(64.dp))
                        .weight(1f)
                        .height(8.dp)
                        .background(ColorBgElevated)
                ) {
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(8.dp)
                            .background(ColorBgPrimary)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(ColorBgSurface)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_activity),
                        tint = ColorIconPrimary,
                        contentDescription = "icon_activity"
                    )

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(R.string.cta_button_group_navigate_to_activity_summary),
                        style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                        color = ColorTextSubtle
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.ic_brace_right),
                    tint = ColorIconElevated,
                    contentDescription = "icon_brace_right"
                )
            }

            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .onLayoutRectChanged(
                        throttleMillis = 50L,
                        debounceMillis = 0L
                    ) { bounds -> lowerAnchorY = bounds.positionInWindow.y - bounds.height }
            )
        }

        Column(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = 0,
                        y = sheetOffsetY.value.toInt()
                    )
                }
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .height(with(LocalDensity.current) { (frameHeight - sheetOffsetY.value).toDp() })
                .bottomSheetSnappable(
                    sheetOffsetY = sheetOffsetY,
                    upperLimit = upperAnchorY,
                    lowerLimit = lowerAnchorY,
                    scope = scope
                )
                .background(ColorBgElevated)
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .size(24.dp)
                        .background(ColorBgSurface)
                ) {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_brace_left),
                        tint = ColorIconDisabled,
                        contentDescription = "icon_brace_left"
                    )
                }

                Text(
                    text = "2025.08.06",
                    style = Head2_B.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextDefault
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .size(24.dp)
                        .background(ColorBgSurface)
                ) {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_brace_right),
                        tint = ColorIconDisabled,
                        contentDescription = "icon_brace_right"
                    )
                }
            }

            TomorrowTimer()
        }
    }
}

@Composable
private fun ColumnScope.TomorrowTimer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(142.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 6.dp.toPx()
                val inset = strokeWidth / 2
                val arcRect = Rect(
                    inset,
                    inset,
                    size.width - inset,
                    size.height - inset
                )

                drawCircle(brush = SolidColor(ColorBgSurface))

                drawArc(
                    brush = SolidColor(ColorBgPrimary),
                    startAngle = -90f,
                    sweepAngle = 120f,
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Butt
                    ),
                    size = arcRect.size,
                    topLeft = arcRect.topLeft
                )
            }
        }

        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = stringResource(R.string.title_launch_from_tomorrow),
            style = Head2_B,
            color = ColorTextDefault
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(R.string.body_launch_from_tomorrow),
            style = Body2_R,
            color = ColorTextSecondary
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF101010
)
@Composable
private fun HomeScreenContentsPreview() {
    HomeScreenContents(
        uiState = HomeUiState(),
        onEvent = {}
    )
}