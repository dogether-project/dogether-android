package site.dogether.presentation.screen.my_page.screen.certification_list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.domain.model.todo.Todo
import site.dogether.domain.model.todo.Todo.Companion.STATUS_APPROVE
import site.dogether.domain.model.todo.Todo.Companion.STATUS_REJECT
import site.dogether.domain.model.todo.Todo.Companion.STATUS_REVIEW_PENDING
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.CertInfoRowItem
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.screen.my_page.screen.certification_list.model.Chip
import site.dogether.presentation.screen.my_page.screen.certification_list.model.SortingMethod
import site.dogether.presentation.theme.Body1_B
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.Body2_S
import site.dogether.presentation.theme.BrushVignetteBottom
import site.dogether.presentation.theme.ColorBgDefault
import site.dogether.presentation.theme.ColorBgDim
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorBorderDisabled
import site.dogether.presentation.theme.ColorBorderSecondary
import site.dogether.presentation.theme.ColorIconElevated
import site.dogether.presentation.theme.ColorIconError
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
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.animateScrollToItemCenteredFixedWidth
import site.dogether.presentation.utils.clickableWithoutRipple
import site.dogether.presentation.utils.toPx
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificationListScreen(viewModel: CertificationListViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value
    val onEvent: (UiEvent) -> Unit = { uiEvent -> viewModel.onEvent(uiEvent) }

    CertificationListScreenContents(
        uiState = uiState,
        onEvent = onEvent
    )

    val selectSortingMethodBottomSheetState = rememberModalBottomSheetState()
    if (uiState.isSelectSortingMethodBottomSheetShowing) {
        SelectSortingMethodBottomSheet(
            sheetState = selectSortingMethodBottomSheetState,
            sortingMethods = SortingMethod.entries,
            currentSortingMethod = uiState.selectedSortingMethod,
            onDismissRequest = { onEvent(CertificationListUiEvent.Callback.OnSelectSortingMethodBottomSheetDismissRequested) },
            onClickSortingMethod = { sortingMethod -> onEvent(CertificationListUiEvent.Click.OnClickSortingMethod(sortingMethod)) }
        )
    }

    BackHandler(uiState.isDetailMode) {
        onEvent(CertificationListUiEvent.Click.OnClickBackButtonWhenDetailMode)
    }
}

@Composable
private fun CertificationListScreenContents(
    uiState: CertificationListUiState,
    onEvent: (UiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.isDetailMode) {
            DetailModeScreen(
                uiState = uiState,
                onEvent = onEvent
            )
        } else {
            if (uiState.myActivity.dailyTodoStats.totalCertificatedCount > 0) {
                TopBar(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    start = { BackButton { onEvent(UiEvent.Click.OnClickBack) } },
                    centerText = stringResource(R.string.title_certification_list)
                )

                CertificationListContents(
                    uiState = uiState,
                    onEvent = onEvent
                )
            } else {
                EmptyCertificationListContents()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnScope.CertificationListContents(
    uiState: CertificationListUiState,
    onEvent: (UiEvent) -> Unit,
) {
    val (dailyTodoStats, certificationsGroupedByTodoCreatedAt, certificationsGroupedByGroupCreatedAt, pageInfo) = uiState.myActivity

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = ColorTextDefault)) {
                    append(stringResource(R.string.body_certification_list_exist_0))
                    append("\n")
                    append(stringResource(R.string.unit_prefix_whole))
                    append(" ")
                }

                withStyle(SpanStyle(color = ColorTextPrimary)) {
                    append("${dailyTodoStats.totalCertificatedCount}")
                    append(stringResource(R.string.unit_each))
                }

                withStyle(SpanStyle(color = ColorTextDefault)) {
                    append(stringResource(R.string.body_certification_list_exist_1))
                }
            },
            style = Head1_B
        )

        Row(
            modifier = Modifier.padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CertificationCountItem(
                icon = painterResource(R.drawable.ic_achieved),
                tint = ColorIconElevated,
                title = stringResource(R.string.common_achieved),
                value = dailyTodoStats.totalCertificatedCount
            )

            CertificationCountItem(
                icon = painterResource(R.drawable.ic_approve_summary),
                tint = ColorIconPrimary,
                title = stringResource(R.string.common_approve),
                value = dailyTodoStats.totalApprovedCount
            )

            CertificationCountItem(
                icon = painterResource(R.drawable.ic_reject_summary),
                tint = ColorIconError,
                title = stringResource(R.string.common_reject),
                value = dailyTodoStats.totalRejectedCount
            )
        }
    }

    LazyRow(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SortingMethodChipItem(
                sortingMethod = uiState.selectedSortingMethod,
                onClick = { onEvent(CertificationListUiEvent.Click.OnClickSelectSortingMethod) }
            )
        }

        items(uiState.chips) { chip ->
            ChipItem(
                chip = chip,
                isSelected = uiState.selectedChip == chip,
                onClick = { onEvent(CertificationListUiEvent.Click.OnClickChip(if (uiState.selectedChip == chip) null else chip)) }
            )
        }
    }

    val gridState = rememberLazyGridState()

    LaunchedEffect(uiState.selectedSortingMethod, uiState.selectedChip) {
        gridState.scrollToItem(0)
    }

    LazyVerticalGrid(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .weight(1f),
        state = gridState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (certificationsGroupedByGroupCreatedAt.isNotEmpty()) {
            certificationsGroupedByGroupCreatedAt.forEach { list ->
                item(
                    key = list.groupName,
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = list.groupName,
                        style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                        color = ColorTextSubtle
                    )
                }

                itemsIndexed(list.certificationInfo) { index, certificationInfo ->
                    CertificationItem(
                        certificationInfo = certificationInfo,
                        onClick = {
                            onEvent(
                                CertificationListUiEvent.Click.OnClickCertificationInfo(
                                    detailedCertifications = list.certificationInfo.toImmutableList(),
                                    index = index
                                )
                            )
                        }
                    )
                }
            }
        }

        if (certificationsGroupedByTodoCreatedAt.isNotEmpty()) {
            certificationsGroupedByTodoCreatedAt.forEach { group ->
                item(
                    key = group.createdAt,
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = "${group.createdAt}(${group.dayOfWeek})",
                        style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                        color = ColorTextSubtle
                    )
                }

                itemsIndexed(
                    items = group.certificationInfo,
                    key = { _, certificationInfo -> certificationInfo.id }
                ) { index, certificationInfo ->
                    CertificationItem(
                        certificationInfo = certificationInfo,
                        onClick = {
                            onEvent(
                                CertificationListUiEvent.Click.OnClickCertificationInfo(
                                    detailedCertifications = group.certificationInfo.toImmutableList(),
                                    index = index
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(gridState, uiState.myActivity.pageInfo, uiState.isLoading) {
        snapshotFlow { gridState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemCount = layoutInfo.totalItemsCount

                if (lastVisibleItemIndex >= totalItemCount - 4 && !uiState.isLoading && pageInfo.hasNext) {
                    onEvent(CertificationListUiEvent.Callback.OnScrollReachedBottom)
                }
            }
    }
}

@Composable
private fun ColumnScope.EmptyCertificationListContents() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(R.drawable.img_dosik_empty),
            contentDescription = "image_dosik_empty"
        )

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(R.string.title_certification_list_not_exist),
            style = Head2_B,
            color = ColorTextSubtle
        )

        Text(
            text = stringResource(R.string.body_certification_list_not_exist),
            style = Body2_R,
            color = ColorTextSecondary
        )
    }
}

@Composable
private fun RowScope.CertificationCountItem(
    icon: Painter,
    tint: Color,
    title: String,
    value: Int,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .weight(1f)
            .aspectRatio(1f)
            .background(ColorBgSurface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = icon,
            tint = tint,
            contentDescription = "icon_certification_count_item"
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = title,
            style = Body2_S,
            color = ColorTextSubtle
        )

        Text(
            text = "$value" + stringResource(R.string.unit_each),
            style = Head2_B,
            color = ColorTextDefault
        )
    }
}

@Composable
private fun SortingMethodChipItem(
    sortingMethod: SortingMethod,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .clickableWithoutRipple { onClick() }
            .border(
                width = 1.dp,
                color = ColorBorderSecondary,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(
                vertical = 6.dp,
                horizontal = 12.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(sortingMethod.stringId),
            style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorTextDefault,
        )

        Icon(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(16.dp),
            painter = painterResource(R.drawable.ic_arrow_down),
            tint = ColorIconPrimary,
            contentDescription = "icon_arrow_down"
        )
    }
}

@Composable
private fun ChipItem(
    modifier: Modifier = Modifier,
    chip: Chip,
    isSelected: Boolean,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(if (isSelected) chip.color else Color.Transparent)
            .clickableWithoutRipple { onClick?.invoke() }
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
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(chip.iconId),
            tint = if (isSelected) ColorBgDefault else ColorIconSecondary,
            contentDescription = "icon_chip"
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = stringResource(chip.stringId),
            style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
            color = if (isSelected) ColorTextInverse else ColorTextSecondary,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectSortingMethodBottomSheet(
    sheetState: SheetState,
    sortingMethods: List<SortingMethod>,
    currentSortingMethod: SortingMethod,
    onClickSortingMethod: (SortingMethod) -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        dragHandle = null,
        containerColor = ColorBgSurface,
        scrimColor = ColorBgDim,
        onDismissRequest = { onDismissRequest() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorBgSurface)
                .padding(
                    top = 24.dp,
                    start = 24.dp,
                    end = 24.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.title_sorting_method),
                    style = Head2_B,
                    color = ColorTextDefault
                )
            }

            sortingMethods.forEach { sortingMethod ->
                SortingMethodItem(
                    sortingMethod = sortingMethod,
                    isSelected = currentSortingMethod == sortingMethod,
                    onClick = { onClickSortingMethod(sortingMethod) }
                )
            }
        }
    }
}

@Composable
private fun SortingMethodItem(
    sortingMethod: SortingMethod,
    isSelected: Boolean,
    onClick: (SortingMethod) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickableWithoutRipple { onClick(sortingMethod) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(sortingMethod.stringId),
            style = Body1_B.copy(lineHeightStyle = LineHeightStyle.Default),
            color = if (isSelected) ColorTextPrimary else ColorTextDisabled
        )

        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                tint = ColorIconPrimary,
                contentDescription = "icon_check"
            )
        }
    }
}

@Composable
private fun CertificationItem(
    certificationInfo: Todo,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .aspectRatio(1f)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = certificationInfo.certificationMediaUrl,
            contentDescription = "certification_image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(ColorBgSurface),
            error = ColorPainter(ColorBgSurface)
        )

        val chip = when (certificationInfo.status) {
            STATUS_REVIEW_PENDING -> Chip.ReviewPending
            STATUS_APPROVE -> Chip.Approve
            STATUS_REJECT -> Chip.Reject
            else -> Chip.ReviewPending
        }

        ChipItem(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
            chip = chip,
            isSelected = true
        )
    }
}

@ScreenPreview
@Composable
private fun CertificationListScreenContentsPreview() {
    CertificationListScreenContents(
        uiState = CertificationListUiState(),
        onEvent = {}
    )
}

@Composable
private fun DetailModeScreen(
    uiState: CertificationListUiState,
    onEvent: (UiEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val context = LocalContext.current
        val lazyListState = rememberLazyListState()
        val itemWidthPx = 48.dp.toPx().roundToInt()

        LaunchedEffect(uiState.selectedItemIndex) {
            lazyListState.animateScrollToItemCenteredFixedWidth(
                index = uiState.selectedItemIndex,
                itemWidthPx = itemWidthPx
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(uiState.myActivity.certificationsGroupedByTodoCompletedAt.size, uiState.selectedItemIndex) {
                    var dragAmountAccumulator = 0f

                    detectHorizontalDragGestures(
                        onDragStart = {
                            dragAmountAccumulator = 0f
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            dragAmountAccumulator += dragAmount
                        },
                        onDragEnd = {
                            val totalCertifications = uiState.detailedCertifications.size
                            if (totalCertifications <= 1) return@detectHorizontalDragGestures

                            val threshold = 50.dp.toPx()

                            if (dragAmountAccumulator > threshold) {
                                if (uiState.selectedItemIndex > 0) {
                                    onEvent(CertificationListUiEvent.Callback.OnSwipeRight)
                                }
                            } else if (dragAmountAccumulator < -threshold) {
                                if (uiState.selectedItemIndex < totalCertifications - 1) {
                                    onEvent(CertificationListUiEvent.Callback.OnSwipeLeft)
                                }
                            }
                        }
                    )
                }
        ) {
            TopBar(
                start = { BackButton { onEvent(UiEvent.Click.OnClickBack) } },
                centerText = uiState.detailTitle
            )

            Column(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .weight(1f)
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    state = lazyListState
                ) {
                    itemsIndexed(uiState.detailedCertifications) { index, todo ->
                        CertInfoRowItem(
                            index = index,
                            isSelected = index == uiState.selectedItemIndex,
                            todo = todo,
                            onClick = { clickedItemIndex ->

                            }
                        )
                    }
                }

                if (uiState.detailedCertifications.isNotEmpty()) {
                    val selectedTodo = uiState.detailedCertifications[uiState.selectedItemIndex]

                    Column(
                        modifier = Modifier
                            .padding(
                                top = 20.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(ColorBgElevated)
                                .border(
                                    width = 1.dp,
                                    color = ColorBorderDisabled,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            if (selectedTodo.certificationMediaUrl.isNotEmpty()) {
                                AsyncImage(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .fillMaxSize(),
                                    model = ImageRequest.Builder(context)
                                        .data(selectedTodo.certificationMediaUrl)
                                        .build(),
                                    contentScale = ContentScale.Inside,
                                    contentDescription = "image_certification"
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(BrushVignetteBottom)
                                )

                                Text(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(
                                            horizontal = 36.dp,
                                            vertical = 16.dp
                                        ),
                                    text = selectedTodo.certificationContent,
                                    style = Body1_R,
                                    color = ColorTextDefault
                                )
                            } else {
                                Image(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .padding(top = 40.dp)
                                        .size(220.dp),
                                    painter = painterResource(R.drawable.img_dosik_empty),
                                    contentDescription = "image_empty"
                                )

                                Text(
                                    modifier = Modifier
                                        .padding(bottom = 30.dp)
                                        .align(Alignment.BottomCenter),
                                    text = stringResource(R.string.body_my_cert_info_not_certified),
                                    style = Body1_R,
                                    color = ColorTextSubtle
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}