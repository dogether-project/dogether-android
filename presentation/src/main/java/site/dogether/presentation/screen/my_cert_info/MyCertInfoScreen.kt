package site.dogether.presentation.screen.my_cert_info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.domain.model.todo.Todo
import site.dogether.domain.model.todo.Todo.Companion.STATUS_CERTIFY_PENDING
import site.dogether.presentation.R
import site.dogether.presentation.Screen
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.CertInfoRowItem
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.screen.my_cert_info.model.Chip
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_S
import site.dogether.presentation.theme.BrushVignetteBottom
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorBorderDisabled
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextInverse
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.LocalNavHostController
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.toPx
import kotlin.math.roundToInt

@Composable
fun MyCertInfoScreen(viewModel: MyCertInfoViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value
    val onEvent: (UiEvent) -> Unit = { uiEvent -> viewModel.onEvent(uiEvent) }
    val navHostController = LocalNavHostController.current

    viewModel.CollectEffect<MyCertInfoUiEffect> { uiEffect ->
        when (uiEffect) {
            is MyCertInfoUiEffect.NavigateToCertificateTodo -> {
                navHostController.navigate("${Screen.CERTIFICATE_TODO}/${uiEffect.todoId}/${uiEffect.todoTitle}")
            }
        }
    }

    MyCertInfoScreenContents(
        uiState = uiState,
        onEvent = onEvent
    )
}

@Composable
private fun MyCertInfoScreenContents(
    uiState: MyCertInfoUiState,
    onEvent: (UiEvent) -> Unit,
) {
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
            .pointerInput(uiState.todos.size, uiState.selectedItemIndex) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()

                    val totalTodos = uiState.todos.size
                    if (totalTodos <= 1) return@detectHorizontalDragGestures

                    if (dragAmount > 0 && uiState.selectedItemIndex > 0) {
                        val newIndex = uiState.selectedItemIndex - 1
                        onEvent(MyCertInfoUiEvent.Click.OnClickItem(newIndex))
                    } else if (dragAmount < 0 && uiState.selectedItemIndex < totalTodos - 1) {
                        val newIndex = uiState.selectedItemIndex + 1
                        onEvent(MyCertInfoUiEvent.Click.OnClickItem(newIndex))
                    }
                }
            }
    ) {
        TopBar(
            start = { BackButton { } },
            centerText = stringResource(R.string.title_my_cert_info)
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
                itemsIndexed(uiState.todos) { index, todo ->
                    CertInfoRowItem(
                        index = index,
                        isSelected = index == uiState.selectedItemIndex,
                        todo = todo,
                        onClick = { clickedItemIndex ->
                            onEvent(MyCertInfoUiEvent.Click.OnClickItem(clickedItemIndex))
                        }
                    )
                }
            }

            if (uiState.todos.isNotEmpty()) {
                val selectedTodo = uiState.todos[uiState.selectedItemIndex]

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
                                contentDescription = null
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
                                contentDescription = null
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

                    val chip = when (selectedTodo.status) {
                        Todo.STATUS_CERTIFY_PENDING -> Chip.CertifyPending
                        Todo.STATUS_REVIEW_PENDING -> Chip.ReviewPending
                        Todo.STATUS_APPROVE -> Chip.Approve
                        Todo.STATUS_REJECT -> Chip.Reject
                        else -> throw Exception()
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(chip.color)
                            .padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        chip.iconId?.let { iconId ->
                            Icon(
                                painter = painterResource(iconId),
                                tint = Color.Black,
                                contentDescription = null
                            )
                        }

                        Text(
                            modifier = Modifier.padding(start = if (chip == Chip.CertifyPending) 0.dp else 5.dp),
                            text = stringResource(chip.stringId),
                            style = Body2_S.copy(
                                lineHeightStyle = LineHeightStyle.Default.copy(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None
                                )
                            ),
                            color = ColorTextInverse
                        )
                    }

                    if (uiState.todos.size > uiState.selectedItemIndex) {
                        Text(
                            modifier = Modifier.padding(top = 12.dp),
                            text = uiState.todos[uiState.selectedItemIndex].content,
                            style = Head1_B,
                            color = ColorTextDefault
                        )

                        if (selectedTodo.reviewFeedback.isNotEmpty()) {
                            Text(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .fillMaxWidth()
                                    .background(ColorBgSurface)
                                    .padding(16.dp),
                                text = selectedTodo.reviewFeedback,
                                style = Body1_S,
                                color = ColorTextDefault
                            )
                        }

                        if (selectedTodo.status == STATUS_CERTIFY_PENDING) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                CTAButton(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(BottomCenter)
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    isEnabled = true,
                                    text = stringResource(R.string.cta_button_certificate),
                                    onClick = { onEvent(MyCertInfoUiEvent.Click.OnClickCertificate) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun LazyListState.animateScrollToItemCenteredFixedWidth(
    index: Int,
    itemWidthPx: Int,
) {
    val viewportWidth = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
    val offset = viewportWidth / 2 - itemWidthPx / 2
    animateScrollToItem(index, scrollOffset = -offset)
}

@ScreenPreview
@Composable
private fun MyCertInfoScreenContentsPreview() {
    MyCertInfoScreenContents(
        uiState = MyCertInfoUiState(),
        onEvent = {}
    )
}