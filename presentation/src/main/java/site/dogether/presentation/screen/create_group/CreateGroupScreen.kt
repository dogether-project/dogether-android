package site.dogether.presentation.screen.create_group

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.ActionDialog
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.DogetherTextField
import site.dogether.presentation.composables.GroupInfoBoard
import site.dogether.presentation.composables.LoadingDialog
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.composables.node.throttledClickable
import site.dogether.presentation.screen.create_group.model.CreateGroupPage
import site.dogether.presentation.theme.Body1_B
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.Body2_S
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorBorderPrimary
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorIconElevated
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextPrimary
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.LocalNavHostController
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.hideKeyboardOnTap

private val PAGE_LIST: List<CreateGroupPage> = CreateGroupPage.entries

@Composable
fun CreateGroupScreen(viewModel: CreateGroupViewModel = koinViewModel()) {
    val navHostController = LocalNavHostController.current
    val uiState = viewModel.collectAsState().value

    viewModel.CollectEffect<CreateGroupUiEffect> { uiEffect ->
        when (uiEffect) {
            is CreateGroupUiEffect.NavigateToBack -> navHostController.popBackStack()
        }
    }

    CreateGroupScreenContents(
        uiState = uiState,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )

    InitDialog(
        uiState = uiState,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

@Composable
private fun CreateGroupScreenContents(
    uiState: CreateGroupUiState,
    onEvent: (UiEvent) -> Unit,
) {
    val pagerState = rememberPagerState { PAGE_LIST.size }

    LaunchedEffect(uiState.currentPage) { pagerState.animateScrollToPage(uiState.currentPage) }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        TopBar(
            start = { BackButton { onEvent(CreateGroupUiEvent.Click.OnClickBack) } },
            centerText = stringResource(R.string.title_create_group)
        )

        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = ColorTextPrimary)) {
                    append("${pagerState.currentPage + 1}")
                }

                withStyle(SpanStyle(color = ColorTextDefault)) {
                    append(" / ${PAGE_LIST.size}")
                }
            },
            style = Body1_S
        )

        HorizontalPager(
            modifier = Modifier.padding(top = 8.dp),
            state = pagerState,
            userScrollEnabled = false,
            pageSpacing = 16.dp
        ) { pageIndex ->
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(PAGE_LIST[pageIndex].titleStringId),
                    style = Head1_B,
                    color = ColorTextDefault
                )

                Spacer(modifier = Modifier.height(40.dp))

                when (PAGE_LIST[pageIndex]) {
                    CreateGroupPage.Purpose -> PurposePageContents(
                        uiState = uiState,
                        onEvent = onEvent
                    )

                    CreateGroupPage.Schedule -> SchedulePageContents(
                        uiState = uiState,
                        onEvent = onEvent
                    )

                    CreateGroupPage.Check -> CheckPageContents(
                        uiState = uiState,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun PurposePageContents(
    uiState: CreateGroupUiState,
    onEvent: (UiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .hideKeyboardOnTap()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.input_title_group_name),
                style = Body1_B,
                color = ColorTextSubtle
            )

            DogetherTextField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                value = uiState.name,
                onValueChanged = { text -> onEvent(CreateGroupUiEvent.Typed.OnGroupNameTyped(text)) },
                hintText = stringResource(R.string.input_hint_group_name),
                lengthLimit = 20
            )

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(R.string.input_title_group_member_count),
                style = Body1_B,
                color = ColorTextSubtle
            )

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(ColorBgElevated)
            ) {
                MaximumMemberCountCalculateButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 5.dp),
                    painter = painterResource(R.drawable.ic_minus),
                    contentDescription = "icon_minus"
                ) { onEvent(CreateGroupUiEvent.Click.OnClickReduceMaximumMemberCount) }

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "${uiState.maximumMemberCount}" + stringResource(R.string.unit_member),
                    style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextDefault
                )

                MaximumMemberCountCalculateButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 5.dp),
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = "icon_plus"
                ) { onEvent(CreateGroupUiEvent.Click.OnClickAddMaximumMemberCount) }
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$MinimumMemberCount" + stringResource(R.string.unit_member),
                    style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextSecondary
                )

                Text(
                    text = "$MaximumMemberCount" + stringResource(R.string.unit_member),
                    style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextSecondary
                )
            }
        }

        CTAButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(50.dp),
            isEnabled = uiState.name.isNotEmpty(),
            radius = 8.dp,
            text = stringResource(R.string.cta_button_next),
            onClick = { onEvent(CreateGroupUiEvent.Click.OnClickNext) }
        )
    }
}

@Composable
private fun MaximumMemberCountCalculateButton(
    modifier: Modifier,
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .size(40.dp)
            .background(ColorBgSurface)
            .throttledClickable { onClick() }
    ) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            painter = painter,
            tint = ColorIconElevated,
            contentDescription = contentDescription
        )
    }
}

@Composable
private fun SchedulePageContents(
    uiState: CreateGroupUiState,
    onEvent: (UiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.input_title_duration),
                style = Body1_B,
                color = ColorTextSubtle
            )

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DurationButton(
                    duration = 3,
                    selectedDuration = uiState.duration,
                    onClick = { onEvent(CreateGroupUiEvent.Click.OnClickDuration(3)) }
                )

                DurationButton(
                    duration = 7,
                    selectedDuration = uiState.duration,
                    onClick = { onEvent(CreateGroupUiEvent.Click.OnClickDuration(7)) }
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DurationButton(
                    duration = 14,
                    selectedDuration = uiState.duration,
                    onClick = { onEvent(CreateGroupUiEvent.Click.OnClickDuration(14)) }
                )

                DurationButton(
                    duration = 28,
                    selectedDuration = uiState.duration,
                    onClick = { onEvent(CreateGroupUiEvent.Click.OnClickDuration(28)) }
                )
            }

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(R.string.input_title_launch_date),
                style = Body1_B,
                color = ColorTextSubtle
            )

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LaunchFromButton(
                    isLaunchFromToday = true,
                    isLaunchFromTodayState = uiState.isLaunchFromToday,
                    onClick = { onEvent(CreateGroupUiEvent.Click.OnClickLaunchFrom(true)) }
                )

                LaunchFromButton(
                    isLaunchFromToday = false,
                    isLaunchFromTodayState = uiState.isLaunchFromToday,
                    onClick = { onEvent(CreateGroupUiEvent.Click.OnClickLaunchFrom(false)) }
                )
            }
        }

        CTAButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(50.dp),
            radius = 8.dp,
            text = stringResource(R.string.cta_button_next),
            onClick = { onEvent(CreateGroupUiEvent.Click.OnClickNext) }
        )
    }
}

@Composable
private fun RowScope.DurationButton(
    duration: Int,
    selectedDuration: Int,
    onClick: () -> Unit,
) {
    val isSelected = duration == selectedDuration

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .weight(1f)
            .background(ColorBgElevated)
            .border(
                width = (1.5).dp,
                color = if (isSelected) ColorBorderPrimary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                start = 16.dp,
                top = 12.dp,
                bottom = 12.dp
            )
            .throttledClickable { onClick() }
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = if (duration < 7) {
                "$duration" + stringResource(R.string.unit_day)
            } else {
                "${duration / 7}" + stringResource(R.string.unit_week)
            },
            style = Body1_B.copy(lineHeightStyle = LineHeightStyle.Default),
            color = if (isSelected) ColorTextPrimary else ColorTextDefault
        )
    }
}

@Composable
private fun RowScope.LaunchFromButton(
    isLaunchFromToday: Boolean,
    isLaunchFromTodayState: Boolean,
    onClick: () -> Unit,
) {
    val isSelected = isLaunchFromToday == isLaunchFromTodayState

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .weight(1f)
            .background(ColorBgElevated)
            .border(
                width = (1.5).dp,
                color = if (isSelected) ColorBorderPrimary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                top = 26.dp,
                bottom = 26.dp,
                start = 20.dp,
            )
            .throttledClickable { onClick() }
    ) {
        Icon(
            painter = painterResource(if (isLaunchFromToday) R.drawable.ic_launch_from_today else R.drawable.ic_launch_from_tomorrow),
            tint = if (isSelected) ColorIconPrimary else ColorIconDefault,
            contentDescription = "icon_launch_From"
        )

        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(if (isLaunchFromToday) R.string.cta_button_title_launch_from_today else R.string.cta_button_title_launch_from_tomorrow),
            style = Body1_B.copy(lineHeightStyle = LineHeightStyle.Default),
            color = if (isSelected) ColorTextPrimary else ColorTextDefault
        )

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(if (isLaunchFromToday) R.string.cta_button_body_launch_from_today else R.string.cta_button_body_launch_from_tomorrow),
            style = Body2_R,
            color = if (isSelected) ColorTextPrimary else ColorTextDefault
        )
    }
}

@Composable
private fun CheckPageContents(
    uiState: CreateGroupUiState,
    onEvent: (UiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(40.dp))

        Column(modifier = Modifier.weight(1f)) {
            GroupInfoBoard(
                modifier = Modifier.padding(horizontal = 32.dp),
                name = uiState.name,
                duration = uiState.duration,
                maximumMemberCount = uiState.maximumMemberCount,
                isLaunchFromToday = uiState.isLaunchFromToday
            )
        }

        CTAButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(50.dp),
            radius = 8.dp,
            text = stringResource(R.string.cta_button_create_group),
            onClick = { onEvent(CreateGroupUiEvent.Click.OnClickCreateGroup) }
        )
    }
}

@Composable
private fun InitDialog(
    uiState: CreateGroupUiState,
    onEvent: (UiEvent) -> Unit,
) {
    if (uiState.duplicatedNameDialogState.isShowing) {
        ActionDialog(
            title = stringResource(R.string.dialog_title_duplicated_name),
            body = stringResource(R.string.dialog_body_duplicated_name),
            icon = painterResource(R.drawable.ic_notice),
            negativeText = stringResource(R.string.dialog_button_back),
            positiveText = stringResource(R.string.dialog_button_create_group),
            onClickNegative = { onEvent(CreateGroupUiEvent.Click.OnClickDuplicatedNameDialogNegative) },
            onClickPositive = { onEvent(CreateGroupUiEvent.Click.OnClickDuplicatedNameDialogPositive) },
            onDismissRequest = { onEvent(CreateGroupUiEvent.Callback.OnDuplicatedNameDialogDismissRequested) }
        )
    }

    if (uiState.isLoading) {
        LoadingDialog()
    }
}

@ScreenPreview
@Composable
private fun CreateGroupScreenContentsPreview() {
    CreateGroupScreenContents(
        uiState = CreateGroupUiState(),
        onEvent = {}
    )
}

@ScreenPreview
@Composable
private fun PurposePageContentsPreview() {
    PurposePageContents(
        uiState = CreateGroupUiState(),
        onEvent = {}
    )
}

@ScreenPreview
@Composable
private fun SchedulePageContentsPreview() {
    SchedulePageContents(
        uiState = CreateGroupUiState(),
        onEvent = {}
    )
}

@ScreenPreview
@Composable
fun CheckPageContentsPreview() {
    CheckPageContents(
        uiState = CreateGroupUiState(),
        onEvent = {}
    )
}