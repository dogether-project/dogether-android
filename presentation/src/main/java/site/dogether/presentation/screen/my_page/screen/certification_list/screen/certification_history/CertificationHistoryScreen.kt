package site.dogether.presentation.screen.my_page.screen.certification_list.screen.certification_history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.CertInfoRowItem
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.screen.certificate.my_cert_info.MyCertInfoUiEvent
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.BrushVignetteBottom
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBorderDisabled
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.utils.animateScrollToItemCenteredFixedWidth
import site.dogether.presentation.utils.toPx
import kotlin.math.roundToInt

@Composable
fun CertificationHistoryScreen(viewModel: CertificationHistoryViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value
    val onEvent: (UiEvent) -> Unit = { uiEvent -> viewModel.onEvent(uiEvent) }

    CertificationHistoryScreenContents(
        uiState = uiState,
        onEvent = onEvent
    )
}

@Composable
private fun CertificationHistoryScreenContents(
    uiState: CertificationHistoryUiState,
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
                .pointerInput(uiState.todos.size, uiState.selectedItemIndex) {
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
                            val totalTodos = uiState.todos.size
                            if (totalTodos <= 1) return@detectHorizontalDragGestures

                            val threshold = 50.dp.toPx()

                            if (dragAmountAccumulator > threshold) {
                                if (uiState.selectedItemIndex > 0) {
                                    onEvent(MyCertInfoUiEvent.Callback.OnSwipeRight)
                                }
                            } else if (dragAmountAccumulator < -threshold) {
                                if (uiState.selectedItemIndex < totalTodos - 1) {
                                    onEvent(MyCertInfoUiEvent.Callback.OnSwipeLeft)
                                }
                            }
                        }
                    )
                }
        ) {
            TopBar(
                start = { BackButton { onEvent(UiEvent.Click.OnClickBack) } },
                centerText = uiState.title
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
                    }
                }
            }
        }
    }
}