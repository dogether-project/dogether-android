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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.DogetherTextField
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.screen.create_group.model.CreateGroupPageItem
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
import site.dogether.presentation.utils.clickableWithoutRipple
import site.dogether.presentation.utils.hideKeyboardOnTap

private val pageList: List<CreateGroupPageItem> = listOf(
    CreateGroupPageItem(
        titleStringId = R.string.title_create_group_purpose,
        content = { PurposePageContents(ctaButtonText = stringResource(R.string.cta_button_next)) },
    ),
    CreateGroupPageItem(
        titleStringId = R.string.title_create_group_schedule,
        content = { SchedulePageContents(ctaButtonText = stringResource(R.string.cta_button_next)) },
    ),
    CreateGroupPageItem(
        titleStringId = R.string.title_create_group_check,
        content = { CheckPageContents(ctaButtonText = stringResource(R.string.cta_button_create_group)) },
    )
)

@Composable
fun CreateGroupScreen(viewModel: CreateGroupViewModel = koinViewModel()) {
    CreateGroupScreenContents()
}

@Composable
private fun CreateGroupScreenContents(viewModel: CreateGroupViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value
    val pagerState = rememberPagerState { pageList.size }

    LaunchedEffect(uiState.currentPage) { pagerState.animateScrollToPage(uiState.currentPage) }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        TopBar(
            start = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    tint = ColorIconDefault,
                    contentDescription = "icon_arrow_back"
                )
            },
            centerText = stringResource(R.string.title_create_group)
        )

        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = ColorTextPrimary)) {
                    append("${pagerState.currentPage + 1}")
                }

                withStyle(SpanStyle(color = ColorTextDefault)) {
                    append(" / ${pageList.size}")
                }
            },
            style = Body1_S
        )

        HorizontalPager(
            modifier = Modifier.padding(top = 8.dp),
            state = pagerState,
            userScrollEnabled = false
        ) { pageIndex ->
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(pageList[pageIndex].titleStringId),
                    style = Head1_B,
                    color = ColorTextDefault
                )

                Spacer(modifier = Modifier.height(40.dp))

                pageList[pageIndex].content()
            }
        }
    }
}

@Composable
private fun PurposePageContents(
    viewModel: CreateGroupViewModel = koinViewModel(),
    ctaButtonText: String,
) {
    val uiState = viewModel.collectAsState().value

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
                value = uiState.groupName,
                onValueChanged = { text -> viewModel.onEvent(CreateGroupUiEvent.Type.OnGroupNameTyped(text)) },
                hintText = stringResource(R.string.input_hint_group_name),
                lengthLimit = 20
            )

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(R.string.input_title_group_member_limit),
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
                MemberLimitCalculateButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 5.dp),
                    painter = painterResource(R.drawable.ic_minus),
                    contentDescription = "icon_minus"
                ) { viewModel.onEvent(CreateGroupUiEvent.Click.OnClickMinusMemberLimit) }

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "${uiState.memberLimit}" + stringResource(R.string.unit_member),
                    style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextDefault
                )

                MemberLimitCalculateButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 5.dp),
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = "icon_plus"
                ) { viewModel.onEvent(CreateGroupUiEvent.Click.OnClickPlusMemberLimit) }
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$MinimumMemberLimit" + stringResource(R.string.unit_member),
                    style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextSecondary
                )

                Text(
                    text = "$MaximumMemberLimit" + stringResource(R.string.unit_member),
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
            isEnabled = uiState.groupName.isNotEmpty(),
            radius = 8.dp,
            text = ctaButtonText,
            onClick = { viewModel.onEvent(CreateGroupUiEvent.Click.OnClickNext) }
        )
    }
}

@Composable
private fun MemberLimitCalculateButton(
    modifier: Modifier,
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .size(40.dp)
            .background(ColorBgSurface)
            .clickableWithoutRipple { onClick() }
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
    viewModel: CreateGroupViewModel = koinViewModel(),
    ctaButtonText: String
) {
    val uiState = viewModel.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.input_title_period),
                style = Body1_B,
                color = ColorTextSubtle
            )

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PeriodButton(
                    period = 3,
                    selectedPeriod = uiState.period,
                    onClick = { viewModel.onEvent(CreateGroupUiEvent.Click.OnClickPeriod(3)) }
                )

                PeriodButton(
                    period = 7,
                    selectedPeriod = uiState.period,
                    onClick = { viewModel.onEvent(CreateGroupUiEvent.Click.OnClickPeriod(7)) }
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PeriodButton(
                    period = 14,
                    selectedPeriod = uiState.period,
                    onClick = { viewModel.onEvent(CreateGroupUiEvent.Click.OnClickPeriod(14)) }
                )

                PeriodButton(
                    period = 28,
                    selectedPeriod = uiState.period,
                    onClick = { viewModel.onEvent(CreateGroupUiEvent.Click.OnClickPeriod(28)) }
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
                    onClick = { viewModel.onEvent(CreateGroupUiEvent.Click.OnClickLaunchFrom(true)) }
                )

                LaunchFromButton(
                    isLaunchFromToday = false,
                    isLaunchFromTodayState = uiState.isLaunchFromToday,
                    onClick = { viewModel.onEvent(CreateGroupUiEvent.Click.OnClickLaunchFrom(false)) }
                )
            }
        }

        CTAButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(50.dp),
            radius = 8.dp,
            text = ctaButtonText,
            onClick = { viewModel.onEvent(CreateGroupUiEvent.Click.OnClickNext) }
        )
    }
}

@Composable
private fun RowScope.PeriodButton(
    period: Int,
    selectedPeriod: Int,
    onClick: () -> Unit
) {
    val isSelected = period == selectedPeriod

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
            .clickableWithoutRipple { onClick() }
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = if (period < 7) {
                "$period" + stringResource(R.string.unit_day)
            } else {
                "${period / 7}" + stringResource(R.string.unit_week)
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
    onClick: () -> Unit
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
            .clickableWithoutRipple { onClick() }
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
    viewModel: CreateGroupViewModel = koinViewModel(),
    ctaButtonText: String
) {

}

@Preview(showBackground = true)
@Composable
private fun CreateGroupScreenContentsPreview() {

}