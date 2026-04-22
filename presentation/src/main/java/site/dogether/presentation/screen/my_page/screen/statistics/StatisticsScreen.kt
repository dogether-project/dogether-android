package site.dogether.presentation.screen.my_page.screen.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.common.MaxDailyTodoCount
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.GroupInfoColumn
import site.dogether.presentation.composables.LoadingDialog
import site.dogether.presentation.composables.NoGroupContents
import site.dogether.presentation.composables.SelectGroupBottomSheet
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.composables.node.skeleton
import site.dogether.presentation.composables.node.throttledClickable
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_S
import site.dogether.presentation.theme.ColorBgDisabled
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBgInverse
import site.dogether.presentation.theme.ColorBgPrimary
import site.dogether.presentation.theme.ColorBorderSecondary
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorIconElevated
import site.dogether.presentation.theme.ColorIconError
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextDisabled
import site.dogether.presentation.theme.ColorTextPrimary
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Emphasis2_B
import site.dogether.presentation.theme.Grey500
import site.dogether.presentation.theme.Grey600
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.ScreenPreview
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value
    val onEvent: (UiEvent) -> Unit = { uiEvent -> viewModel.onEvent(uiEvent) }

    viewModel.CollectEffect<StatisticsUiEffect> { uiEffect ->
        when (uiEffect) {
            else -> Unit
        }
    }

    StatisticsScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = viewModel::onEvent
    )

    val selectGroupBottomSheetState = rememberModalBottomSheetState()
    if (uiState.isSelectGroupBottomSheetShowing) {
        SelectGroupBottomSheet(
            sheetState = selectGroupBottomSheetState,
            selectedGroup = uiState.selectedGroup,
            groups = uiState.groups,
            isAddButtonShowing = false,
            onDismissRequest = { onEvent(StatisticsUiEvent.Callback.OnSelectGroupBottomSheetDismissRequested) },
            onClickGroupItem = { onEvent(StatisticsUiEvent.Click.OnClickGroupItem(it)) },
            onClickAddGroup = { }
        )
    }
}

@Composable
private fun StatisticsScreenContents(
    uiState: StatisticsUiState,
    onEvent: (UiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        TopBar(
            start = { BackButton { onEvent(UiEvent.Click.OnClickBack) } },
            centerText = stringResource(R.string.title_statistics)
        )

        if (uiState.isGroupsLoading) {
            LoadingDialog()
        } else {
            if (uiState.groups.isNotEmpty()) {
                StatisticsContents(
                    uiState = uiState,
                    onEvent = onEvent
                )
            } else {
                NoGroupContents(onClickCreateGroup = { onEvent(StatisticsUiEvent.Click.OnClickCreateGroup) })
            }
        }
    }
}

@Composable
private fun StatisticsContents(
    uiState: StatisticsUiState,
    onEvent: (UiEvent) -> Unit,
) {
    val (group, _, ranking, stats) = uiState.groupStatistics

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .skeleton(
                        condition = uiState.isStatisticsLoading,
                        widthDp = 200.dp,
                        heightDp = 36.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = group.name,
                    style = Head1_B.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextPrimary
                )

                Icon(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .throttledClickable { onEvent(StatisticsUiEvent.Click.OnClickSelectGroup) },
                    painter = painterResource(R.drawable.ic_arrow_down),
                    tint = ColorIconElevated,
                    contentDescription = "icon_arrow_down"
                )
            }

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GroupInfoColumn(
                    isLoading = uiState.isStatisticsLoading,
                    title = stringResource(R.string.info_group_member_count),
                    value = "${group.currentMemberCount}/${group.maximumMemberCount}",
                )

                GroupInfoColumn(
                    isLoading = uiState.isStatisticsLoading,
                    title = stringResource(R.string.info_join_code),
                    value = group.joinCode
                )

                GroupInfoColumn(
                    isLoading = uiState.isStatisticsLoading,
                    title = stringResource(R.string.info_end_date),
                    value = group.endAt
                )
            }
        }

        Image(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 36.dp)
                .width(100.dp)
                .height(126.dp),
            painter = painterResource(R.drawable.img_dosik_behind),
            contentDescription = "image_dosik_behind_surface"
        )

        Column(
            modifier = Modifier
                .padding(top = 117.dp)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .background(ColorBgElevated)
                .padding(
                    horizontal = 16.dp,
                    vertical = 20.dp
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_certification_duration),
                    tint = ColorIconDefault,
                    contentDescription = "icon_certification_duration"
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(R.string.info_certification_duration),
                    style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextDefault
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .width(35.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    (MaxDailyTodoCount downTo 0 step 2).forEach { todoCount ->
                        Text(
                            text = "$todoCount",
                            style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
                            color = ColorTextDisabled
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(end = 36.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val graphHeight = 180
                    val maxSlots = 4
                    val heightPerTodo = graphHeight / MaxDailyTodoCount.toFloat()

                    repeat(maxSlots) { index ->
                        val certificationStatistics = uiState.displayCertificationPeriods.getOrNull(index)

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .height(graphHeight.dp)
                                    .width(50.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                if (certificationStatistics != null) {
                                    val currentBarHeight = if (uiState.isStatisticsLoading) graphHeight.dp
                                    else (certificationStatistics.createdCount * heightPerTodo).dp

                                    Box(
                                        modifier = Modifier
                                            .width(50.dp)
                                            .height(currentBarHeight)
                                            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                                            .background(Grey600)
                                            .hatch(Grey500)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .fillMaxWidth()
                                                .height((certificationStatistics.certificatedCount * heightPerTodo).dp)
                                                .background(ColorBgPrimary)
                                        )
                                    }

                                    if (index == uiState.displayCertificationPeriods.lastIndex) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .offset(y = -currentBarHeight)
                                                .wrapContentWidth(unbounded = true, align = Alignment.CenterHorizontally)
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier
                                                    .width(IntrinsicSize.Max)
                                                    .offset(y = 6.5.dp)
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .background(
                                                            color = ColorBgPrimary,
                                                            shape = RoundedCornerShape(16.dp)
                                                        )
                                                        .padding(horizontal = 10.dp, vertical = 4.5.dp),
                                                    text = stringResource(R.string.statistics_description_achieve_ratio, uiState.groupStatistics.certificationPeriods[index].certificationRate),
                                                    style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default.copy(trim = LineHeightStyle.Trim.Both)),
                                                    color = ColorTextDefault,
                                                    softWrap = false
                                                )

                                                Canvas(
                                                    modifier = Modifier
                                                        .width(8.dp)
                                                        .height(6.dp)
                                                ) {
                                                    val path = Path().apply {
                                                        moveTo(0f, 0f)
                                                        lineTo(size.width, 0f)
                                                        lineTo(size.width / 2f, size.height)
                                                        close()
                                                    }
                                                    drawPath(path = path, color = ColorBgPrimary)
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .padding(top = 2.dp)
                                                        .clip(CircleShape)
                                                        .size(13.dp)
                                                        .background(ColorBgInverse)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .align(Alignment.Center)
                                                            .clip(CircleShape)
                                                            .size(9.dp)
                                                            .background(ColorBgPrimary)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(50.dp, graphHeight.dp))
                                }
                            }

                            if (certificationStatistics != null) {
                                Text(
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .skeleton(
                                            condition = uiState.isStatisticsLoading,
                                            widthDp = 50.dp
                                        ),
                                    text = "${certificationStatistics.day}${stringResource(R.string.unit_day_passed)}",
                                    style = Body2_S,
                                    color = ColorTextDefault
                                )
                            } else {
                                Spacer(modifier = Modifier
                                    .padding(top = 10.dp)
                                    .height(20.dp)
                                    .width(50.dp))
                            }
                        }
                    }
                }
            }
        }

        Image(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 107.dp,
                    end = (4.47).dp
                )
                .width((91.53).dp)
                .height((28.45).dp),
            painter = painterResource(R.drawable.img_dosik_arms),
            contentDescription = "image_dosik_arms"
        )
    }

    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .height(180.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .weight(1f)
                .fillMaxHeight()
                .background(ColorBgElevated)
                .padding(
                    horizontal = 16.dp,
                    vertical = 20.dp
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_rank),
                    tint = ColorIconDefault,
                    contentDescription = "icon_rank"
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(R.string.info_my_rank),
                    style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextDefault
                )
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.skeleton(
                        condition = uiState.isStatisticsLoading,
                        color = ColorBgDisabled,
                    ),
                    text = ranking.totalMemberCount.toString() + stringResource(R.string.unit_member) + " " + stringResource(R.string.unit_postfix_total),
                    style = Body1_S,
                    color = ColorTextSubtle
                )

                Text(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .skeleton(
                            condition = uiState.isStatisticsLoading,
                            color = ColorBgDisabled,
                        ),
                    text = ranking.myRank.toString() + stringResource(R.string.unit_rank),
                    style = Emphasis2_B.copy(lineHeightStyle = LineHeightStyle.Default.copy(trim = LineHeightStyle.Trim.FirstLineTop)),
                    color = ColorTextPrimary
                )
            }
        }

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .weight(1f)
                .fillMaxHeight()
                .background(ColorBgElevated)
                .padding(
                    horizontal = 16.dp,
                    vertical = 20.dp
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_summary),
                    tint = ColorIconDefault,
                    contentDescription = "icon_summary"
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(R.string.info_summary),
                    style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextDefault
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryItem(
                    isLoading = uiState.isStatisticsLoading,
                    icon = painterResource(R.drawable.ic_achieved),
                    tint = ColorIconElevated,
                    title = stringResource(R.string.common_achieved),
                    value = stats.certificatedCount
                )

                SummaryItem(
                    isLoading = uiState.isStatisticsLoading,
                    icon = painterResource(R.drawable.ic_approve_summary),
                    tint = ColorIconPrimary,
                    title = stringResource(R.string.common_approve),
                    value = stats.approvedCount
                )

                SummaryItem(
                    isLoading = uiState.isStatisticsLoading,
                    icon = painterResource(R.drawable.ic_reject_summary),
                    tint = ColorIconError,
                    title = stringResource(R.string.common_reject),
                    value = stats.rejectedCount
                )
            }
        }
    }
}

fun Modifier.hatch(color: Color = ColorBorderSecondary) = this.then(
    Modifier.drawWithCache {
        val step = 10.dp.toPx()
        val stroke = 2.dp.toPx()

        val w = size.width
        val h = size.height
        val diagonal = sqrt(w * w + h * h)

        fun DrawScope.drawStripeLines() {
            var x = -diagonal
            while (x <= diagonal) {
                drawLine(
                    color = color,
                    start = Offset(x, -diagonal),
                    end = Offset(x, diagonal),
                    strokeWidth = stroke
                )
                x += step
            }
        }

        onDrawWithContent {
            withTransform({ rotate(24f, pivot = center) }) { drawStripeLines() }
            drawContent()
        }
    }
)

@Composable
private fun SummaryItem(
    isLoading: Boolean,
    icon: Painter,
    tint: Color,
    title: String,
    value: Int,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = icon,
            tint = tint,
            contentDescription = "icon_summary_item"
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = title,
            style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorTextSubtle
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .skeleton(
                    condition = isLoading,
                    widthDp = 64.dp,
                    color = ColorBgDisabled
                ),
            text = "$value" + stringResource(R.string.unit_each),
            style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorTextDefault
        )
    }
}

@ScreenPreview
@Composable
private fun StatisticsScreenContentsPreview() {
    StatisticsScreenContents(
        uiState = StatisticsUiState(),
        onEvent = {}
    )
}