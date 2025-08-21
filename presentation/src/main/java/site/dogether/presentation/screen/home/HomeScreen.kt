package site.dogether.presentation.screen.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onLayoutRectChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.common.MaxDailyTodoCount
import site.dogether.presentation.R
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.SelectGroupBottomSheet
import site.dogether.presentation.composables.GroupInfoColumn
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.model.Todo
import site.dogether.presentation.model.Todo.Companion.STATUS_APPROVE
import site.dogether.presentation.model.Todo.Companion.STATUS_CERTIFY_PENDING
import site.dogether.presentation.model.Todo.Companion.STATUS_REJECT
import site.dogether.presentation.model.Todo.Companion.STATUS_REVIEW_PENDING
import site.dogether.presentation.screen.home.model.Chip
import site.dogether.presentation.screen.home.state.AnchoredBottomSheetState
import site.dogether.presentation.screen.home.state.PersistentTooltipStateImpl
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.Body2_S
import site.dogether.presentation.theme.ColorBgDefault
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBgInverse
import site.dogether.presentation.theme.ColorBgPrimary
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorBorderSecondary
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorIconDisabled
import site.dogether.presentation.theme.ColorIconElevated
import site.dogether.presentation.theme.ColorIconError
import site.dogether.presentation.theme.ColorIconInverse
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorIconSecondary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextDisabled
import site.dogether.presentation.theme.ColorTextInverse
import site.dogether.presentation.theme.ColorTextPrimary
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.theme.Head2_B
import site.dogether.presentation.theme.Red400
import site.dogether.presentation.theme.Small_R
import site.dogether.presentation.theme.Small_S
import site.dogether.presentation.theme.Yellow
import site.dogether.presentation.utils.DATE_FORMAT_FULL_YEAR
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.alphaByProgress
import site.dogether.presentation.utils.bottomSheetSnappable
import site.dogether.presentation.utils.clickableWithoutRipple
import site.dogether.presentation.utils.toDp
import site.dogether.presentation.utils.toFormattedString
import site.dogether.presentation.utils.today
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    HomeScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContents(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tooltipState = remember { PersistentTooltipStateImpl() }
    val density = LocalDensity.current
    val anchoredBottomSheetState = remember { AnchoredBottomSheetState() }
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < 0f && anchoredBottomSheetState.sheetOffsetY.value > anchoredBottomSheetState.upperAnchorY) {
                    val before = anchoredBottomSheetState.sheetOffsetY.value
                    scope.launch {
                        anchoredBottomSheetState.setOffset(before + available.y)
                    }
                    return Offset(x = 0f, y = available.y)
                }
                return Offset.Zero
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                if (available.y > 0f && anchoredBottomSheetState.sheetOffsetY.value < anchoredBottomSheetState.lowerAnchorY) {
                    val before = anchoredBottomSheetState.sheetOffsetY.value
                    scope.launch {
                        anchoredBottomSheetState.setOffset(before + available.y)
                    }
                    return Offset(x = 0f, y = available.y)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (available.y < 0f && anchoredBottomSheetState.sheetOffsetY.value > anchoredBottomSheetState.upperAnchorY) {
                    anchoredBottomSheetState.animateToUpper()
                    return available
                }
                return Velocity.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (available.y > 0f && anchoredBottomSheetState.sheetOffsetY.value < anchoredBottomSheetState.lowerAnchorY) {
                    anchoredBottomSheetState.animateToLower()
                    return available
                }
                return Velocity.Zero
            }
        }
    }

    WindowInsets.systemBars.getTop(density).takeIf { it != 0 }?.let {
        anchoredBottomSheetState.statusBarHeight = it
    }

    LaunchedEffect(anchoredBottomSheetState.lowerAnchorY) {
        anchoredBottomSheetState.snapToLowerAnchorIfNeeded()
    }

    LaunchedEffect(uiState.tooltipUiState.isShowing) {
        if (uiState.tooltipUiState.isShowing) {
            tooltipState.forceShow()
        } else {
            tooltipState.forceDismiss()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onLayoutRectChanged(
                throttleMillis = 50L,
                debounceMillis = 0L
            ) { bounds -> anchoredBottomSheetState.frameHeight = bounds.height }
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
                    Row(
                        modifier = Modifier.padding(top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DND 작심삼일 탈출러",
                            style = Head1_B.copy(lineHeightStyle = LineHeightStyle.Default),
                            color = ColorTextPrimary
                        )

                        Icon(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .clickableWithoutRipple { onEvent(HomeUiEvent.Click.OnClickSelectGroup) },
                            painter = painterResource(R.drawable.ic_arrow_down),
                            tint = ColorIconElevated,
                            contentDescription = "icon_arrow_down"
                        )
                    }

                    Row(
                        modifier = Modifier
                            .alphaByProgress(anchoredBottomSheetState.expandingProgress)
                            .padding(top = 12.dp)
                            .onLayoutRectChanged { bounds -> anchoredBottomSheetState.upperAnchorY = bounds.positionInWindow.y },
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GroupInfoColumn(
                            title = stringResource(R.string.info_group_member_limit),
                            value = "6/10",
                        )

                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = stringResource(R.string.info_invite_code),
                                style = Body2_R.copy(lineHeightStyle = LineHeightStyle.Default.copy(trim = LineHeightStyle.Trim.None)),
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

                        GroupInfoColumn(
                            title = stringResource(R.string.info_end_date),
                            value = "25.02.22"
                        )
                    }
                }

                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        DosikTooltip(
                            modifier = Modifier.alphaByProgress(anchoredBottomSheetState.expandingProgress),
                            text = uiState.tooltipUiState.stringId?.let { stringResource(it) }.orEmpty(),
                            onClickDismiss = { }
                        )
                    },
                    state = tooltipState,
                    focusable = false
                ) {
                    Image(
                        modifier = Modifier
                            .alphaByProgress(anchoredBottomSheetState.expandingProgress)
                            .size(100.dp),
                        painter = painterResource(R.drawable.img_dosik_main),
                        contentDescription = "image_dosik_main"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .alphaByProgress(anchoredBottomSheetState.expandingProgress)
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
                    .alphaByProgress(anchoredBottomSheetState.expandingProgress)
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
                    .background(Yellow)
                    .onLayoutRectChanged(
                        throttleMillis = 50L,
                        debounceMillis = 0L
                    ) { bounds -> anchoredBottomSheetState.lowerAnchorY = bounds.positionInWindow.y + bounds.height }
            )
        }

        Column(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = 0,
                        y = anchoredBottomSheetState.sheetOffsetY.value.roundToInt() - anchoredBottomSheetState.statusBarHeight
                    )
                }
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .fillMaxWidth()
                .height(
                    (anchoredBottomSheetState.frameHeight
                            - anchoredBottomSheetState.sheetOffsetY.value
                            + anchoredBottomSheetState.statusBarHeight).toDp()
                )
                .nestedScroll(connection)
                .bottomSheetSnappable(
                    sheetOffsetY = anchoredBottomSheetState.sheetOffsetY,
                    upperLimit = anchoredBottomSheetState.upperAnchorY,
                    lowerLimit = anchoredBottomSheetState.lowerAnchorY,
                    scope = scope
                )
                .background(ColorBgElevated)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
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
                    text = today.toFormattedString(DATE_FORMAT_FULL_YEAR),
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

//            LaunchFromTomorrowContents(
//                uiState = uiState,
//                onEvent = onEvent
//            )

//            TodoContents(
//                uiState = uiState,
//                onEvent = onEvent
//            )

//            NoTodoContents()

//            FinishedContents()
        }

        if (uiState.isSelectGroupBottomSheetExpanded) {
            SelectGroupBottomSheet(
                sheetState = rememberModalBottomSheetState(),
                currentGroup = uiState.currentGroup,
                groupList = uiState.groupList,
                isAddButtonShowing = true,
                onDismissRequest = { onEvent(HomeUiEvent.Callback.OnSelectGroupBottomSheetDismissRequested) },
                onClickGroupItem = { }
            )
        }
    }
}

@Composable
private fun DosikTooltip(
    modifier: Modifier,
    text: String,
    onClickDismiss: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(ColorBgInverse)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = Small_S.copy(
                    lineHeightStyle = LineHeightStyle.Default.copy(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    )
                ),
                color = ColorTextInverse
            )

            Icon(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(12.dp)
                    .clickableWithoutRipple { onClickDismiss() },
                painter = painterResource(R.drawable.ic_close),
                tint = ColorIconInverse,
                contentDescription = "icon_close"
            )
        }

        Canvas(
            modifier = Modifier
                .size(10.dp)
                .offset(x = (-36).dp)
        ) {
            val base = 10.dp.toPx()
            val height = (base * sqrt(3f) / 2f)

            val path = Path().apply {
                moveTo(
                    x = 0f,
                    y = 0f
                )
                lineTo(
                    x = base,
                    y = 0f
                )
                lineTo(
                    x = base / 2f,
                    y = height
                )
                close()
            }

            drawPath(
                path = path,
                color = ColorBgInverse
            )
        }
    }
}

@Composable
private fun LaunchFromTomorrowContents(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(142.dp),
            contentAlignment = Alignment.Center
        ) {
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
                    sweepAngle = uiState.timerProgress * 360f,
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    ),
                    size = arcRect.size,
                    topLeft = arcRect.topLeft
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.ic_timer),
                    tint = ColorIconPrimary,
                    contentDescription = "icon_timer"
                )

                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = uiState.timerText,
                    style = Head1_B.copy(fontFeatureSettings = "tnum"),
                    color = ColorTextDefault
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

@Composable
private fun TodoContents(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.todoList.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipItem(
                    chip = Chip.All,
                    isSelected = Chip.All == uiState.selectedChip,
                    color = ColorBgPrimary,
                    onClick = { onEvent(HomeUiEvent.Click.OnClickChip(Chip.All)) }
                )

                ChipItem(
                    chip = Chip.ReviewPending,
                    icon = painterResource(R.drawable.ic_review_pending),
                    isSelected = Chip.ReviewPending == uiState.selectedChip,
                    color = Yellow,
                    onClick = { onEvent(HomeUiEvent.Click.OnClickChip(Chip.ReviewPending)) }
                )

                ChipItem(
                    chip = Chip.Approve,
                    icon = painterResource(R.drawable.ic_approve),
                    isSelected = Chip.Approve == uiState.selectedChip,
                    color = ColorBgPrimary,
                    onClick = { onEvent(HomeUiEvent.Click.OnClickChip(Chip.Approve)) }
                )

                ChipItem(
                    chip = Chip.Reject,
                    icon = painterResource(R.drawable.ic_reject),
                    isSelected = Chip.Reject == uiState.selectedChip,
                    color = Red400,
                    onClick = { onEvent(HomeUiEvent.Click.OnClickChip(Chip.Reject)) }
                )
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.filteredTodoList.forEach { todo -> TodoItem(todo) }

                Row(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add_todo),
                        tint = ColorIconElevated,
                        contentDescription = "icon_add_todo"
                    )

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(R.string.cta_button_add_todo) + " (${uiState.todoList.size}/$MaxDailyTodoCount)",
                        style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                        color = ColorTextSubtle
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.padding(horizontal = 64.dp),
                    painter = painterResource(R.drawable.img_first_day_contents),
                    contentDescription = "image_first_day_contents"
                )

                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = stringResource(R.string.title_create_todo_from_today),
                    style = Head2_B,
                    color = ColorTextDefault
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(R.string.body_create_todo_from_today),
                    style = Body2_R,
                    color = ColorTextSecondary
                )
            }

            CTAButton(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                radius = 8.dp,
                text = stringResource(R.string.cta_button_create_todo),
                onClick = {}
            )
        }
    }
}

@Composable
private fun ChipItem(
    chip: Chip,
    icon: Painter? = null,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(if (isSelected) color else Color.Transparent)
            .clickableWithoutRipple { onClick() }
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Transparent else ColorBorderSecondary,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(
                vertical = 6.dp,
                horizontal = 12.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let { icon ->
            Icon(
                painter = icon,
                tint = if (isSelected) ColorBgDefault else ColorIconSecondary,
                contentDescription = "icon_chip"
            )

            Spacer(modifier = Modifier.width(4.dp))
        }

        Text(
            text = stringResource(chip.stringId),
            style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
            color = if (isSelected) ColorTextInverse else ColorTextSecondary,
        )
    }
}

@Composable
private fun TodoItem(todo: Todo) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .height(64.dp)
            .background(ColorBgSurface)
            .padding(horizontal = 16.dp)
            .clickableWithoutRipple { },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon = when (todo.status) {
            STATUS_REVIEW_PENDING -> painterResource(R.drawable.ic_review_pending)
            STATUS_APPROVE -> painterResource(R.drawable.ic_approve)
            STATUS_REJECT -> painterResource(R.drawable.ic_reject)
            else -> null
        }

        val tint = when (todo.status) {
            STATUS_REVIEW_PENDING -> Yellow
            STATUS_APPROVE -> ColorIconPrimary
            STATUS_REJECT -> ColorIconError
            else -> Color.Transparent
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (todo.status != STATUS_CERTIFY_PENDING) {
                icon?.let {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = icon,
                        tint = tint,
                        contentDescription = "icon_status"
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = todo.content,
                style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                color = if (todo.status == STATUS_CERTIFY_PENDING) ColorTextDefault else ColorTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (todo.status == STATUS_CERTIFY_PENDING) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorBgPrimary)
                    .padding(
                        horizontal = 12.dp,
                        vertical = (3.5).dp
                    )
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.cta_button_certificate),
                    style = Body2_S.copy(
                        lineHeightStyle = LineHeightStyle.Default.copy(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    ),
                    color = ColorTextInverse
                )
            }
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_brace_right),
                tint = ColorIconElevated,
                contentDescription = "icon_brace_right"
            )
        }
    }
}

@Composable
private fun NoTodoContents() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(R.drawable.img_no_todo),
            contentDescription = "image_no_todo"
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.title_no_todo),
            style = Head2_B,
            color = ColorTextSubtle
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(R.string.body_no_todo),
            style = Body2_R,
            color = ColorTextDisabled
        )
    }
}

@Composable
private fun FinishedContents() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.padding(horizontal = 25.dp),
            painter = painterResource(R.drawable.img_finished),
            contentDescription = "image_finished"
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.title_group_finished),
            style = Head2_B,
            color = ColorTextDefault
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(R.string.body_group_finished),
            style = Body2_R,
            color = ColorTextSecondary
        )
    }
}

@ScreenPreview
@Composable
private fun HomeScreenContentsPreview() {
    HomeScreenContents(
        uiState = HomeUiState(),
        onEvent = {}
    )
}