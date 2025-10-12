package site.dogether.presentation.screen.my_cert_info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.CertInfoRowItem
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.Body2_S
import site.dogether.presentation.theme.ColorBgDefault
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBorderDisabled
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextInverse
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.toPx
import kotlin.math.roundToInt

@Composable
fun MyCertInfoScreen(viewModel: MyCertInfoViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value
    val onEvent: (UiEvent) -> Unit = { uiEvent -> viewModel.onEvent(uiEvent) }

    viewModel.CollectEffect<MyCertInfoUiEffect> { uiEffect ->
        when (uiEffect) {
            else -> Unit
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
    onEvent: (UiEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val itemWidthPx = 48.dp.toPx().roundToInt()

    LaunchedEffect(uiState.selectedItemIndex) {
        lazyListState.animateScrollToItemCenteredFixedWidth(
            index = uiState.selectedItemIndex,
            itemWidthPx = itemWidthPx
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
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
                itemsIndexed(uiState.items) { index, item -> // todo
                    CertInfoRowItem(
                        index = index,
                        isSelected = index == uiState.selectedItemIndex,
                        onClick = { clickedItemIndex ->
                            onEvent(MyCertInfoUiEvent.Click.OnClickItem(clickedItemIndex))
                        }
                    )
                }
            }

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

                Row(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(uiState.chip.color)
                        .padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(uiState.chip.iconId),
                        tint = ColorBgDefault,
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = stringResource(uiState.chip.stringId),
                        style = Body2_S.copy(
                            lineHeightStyle = LineHeightStyle.Default.copy(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.None
                            )
                        ),
                        color = ColorTextInverse
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = uiState.title,
                    style = Head1_B,
                    color = ColorTextDefault
                )
            }
        }
    }
}

suspend fun LazyListState.animateScrollToItemCenteredFixedWidth(
    index: Int,
    itemWidthPx: Int
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