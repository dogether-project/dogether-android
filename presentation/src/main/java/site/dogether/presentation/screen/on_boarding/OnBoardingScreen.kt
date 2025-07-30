package site.dogether.presentation.screen.on_boarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel
import site.dogether.presentation.R
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.screen.on_boarding.model.OnBoardingPageItem
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorIconDisabled
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B

private val pageList: List<OnBoardingPageItem> = listOf(
    OnBoardingPageItem(
        titleStringId = R.string.title_on_boarding_0,
        bodyStringId = R.string.body_on_boarding_0,
        imageId = R.drawable.img_on_boarding_0
    ),
    OnBoardingPageItem(
        titleStringId = R.string.title_on_boarding_1,
        bodyStringId = R.string.body_on_boarding_1,
        imageId = R.drawable.img_on_boarding_1
    ),
    OnBoardingPageItem(
        titleStringId = R.string.title_on_boarding_2,
        bodyStringId = R.string.body_on_boarding_2,
        imageId = R.drawable.img_on_boarding_2
    )
)

@Composable
fun OnBoardingScreen() {
    OnBoardingScreenContents()
}

@Composable
private fun OnBoardingScreenContents(viewModel: OnBoardingViewModel = koinViewModel()) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { pageList.size }
            )

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    modifier = Modifier.fillMaxWidth(),
                    state = pagerState
                ) { pageIndex ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(pageList[pageIndex].titleStringId),
                            style = Head1_B,
                            textAlign = TextAlign.Center,
                            color = ColorTextDefault
                        )

                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = stringResource(pageList[pageIndex].bodyStringId),
                            style = Body1_R,
                            textAlign = TextAlign.Center,
                            color = ColorTextSubtle
                        )

                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(pageList[pageIndex].imageId),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = "image_on_boarding"
                        )
                    }
                }

                PagerIndicator(
                    modifier = Modifier.padding(top = 20.dp),
                    currentPageIndex = pagerState.currentPage,
                    pageCount = pagerState.pageCount
                )
            }
        }

        CTAButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(50.dp),
            radius = 8.dp,
            text = stringResource(R.string.cta_button_kakao_login),
            onClick = { viewModel.onEvent(OnBoardingUiEvent.Click.OnClickKakaoLogin) }
        )
    }
}

@Composable
private fun PagerIndicator(
    modifier: Modifier,
    currentPageIndex: Int,
    pageCount: Int,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(8.dp)
                    .background(if (index == currentPageIndex) ColorIconDefault else ColorIconDisabled)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnBoardingScreenContentsPreview() {
    OnBoardingScreenContents(viewModel())
}