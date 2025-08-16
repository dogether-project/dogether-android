package site.dogether.presentation.screen.my_page.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.ColorBgPrimary
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorTextBlack
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head2_B
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.clickableWithoutRipple

@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = koinViewModel()) {
    StatisticsScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

@Composable
private fun StatisticsScreenContents(
    uiState: StatisticsUiState,
    onEvent: (StatisticsUiEvent) -> Unit
) {
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
            centerText = stringResource(R.string.title_statistics)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(150.dp),
                painter = painterResource(R.drawable.img_dosik_empty),
                contentDescription = "image_dosik_empty"
            )

            Text(
                modifier = Modifier.padding(top = 32.dp),
                text = stringResource(R.string.title_no_group),
                style = Head2_B,
                color = ColorTextSubtle
            )

            Text(
                text = stringResource(R.string.body_no_group),
                style = Body2_R,
                color = ColorTextSecondary
            )

            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorBgPrimary)
                    .padding(
                        vertical = 12.dp,
                        horizontal = 40.dp
                    )
                    .clickableWithoutRipple {},
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.cta_button_create_group_2),
                    style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default.copy(trim = LineHeightStyle.Trim.None)),
                    color = ColorTextBlack
                )
            }
        }
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