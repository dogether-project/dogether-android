package site.dogether.presentation.screen.on_boarding

import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.Screen
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.on_boarding.model.OnBoardingPage
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorIconDisabled
import site.dogether.presentation.theme.ColorKakaoLabel
import site.dogether.presentation.theme.ColorKakaoLogo
import site.dogether.presentation.theme.ColorKakaoYellow
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.LocalNavHostController
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.clickableWithoutRipple

private val PAGE_LIST: List<OnBoardingPage> = OnBoardingPage.entries

@Composable
fun OnBoardingScreen(viewModel: OnBoardingViewModel = koinViewModel()) {
    val onEvent: (UiEvent) -> Unit = { uiEvent -> viewModel.onEvent(uiEvent) }
    val context = LocalContext.current
    val navHostController = LocalNavHostController.current

    viewModel.CollectEffect<OnBoardingUiEffect> { uiEffect ->
        when (uiEffect) {
            is OnBoardingUiEffect.CheckLoginWithKakaoTalkPossibility -> {
                val isPossible = UserApiClient.instance.isKakaoTalkLoginAvailable(context)
                onEvent(OnBoardingUiEvent.Callback.OnLoginWithKakaoTalkPossible(isPossible))
            }

            is OnBoardingUiEffect.LoginWithKakaoTalk -> {
                loginWithKakaoTalk(
                    context = context,
                    onEvent = onEvent
                )
            }

            is OnBoardingUiEffect.LoginWithKakaoAccount -> {
                loginWithKakaoAccount(
                    context = context,
                    onEvent = onEvent
                )
            }

            is OnBoardingUiEffect.NavigateToHome -> navigateToHome(navHostController)

            is OnBoardingUiEffect.NavigateToParticipationMethod -> navigateToParticipationMethod(navHostController)
        }
    }

    OnBoardingScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = onEvent
    )
}

private fun kakaoLoginCallback(onEvent: (UiEvent) -> Unit): (OAuthToken?, Throwable?) -> Unit = { token, error ->
    error?.let { throwable ->
        onEvent(OnBoardingUiEvent.Callback.OnErrorKakaoLogin(throwable))
    } ?: run {
        token?.let {
            val idToken = token.idToken.orEmpty()
            UserApiClient.instance.me { user, meError ->

                meError?.let {
                    onEvent(OnBoardingUiEvent.Callback.OnErrorKakaoLogin(meError))
                } ?: run {
                    user?.let {
                        val name = user.kakaoAccount?.profile?.nickname.orEmpty()
                        onEvent(OnBoardingUiEvent.Callback.OnSuccessKakaoLogin(name, idToken))
                    }
                }
            }
        }
    }
}

private fun loginWithKakaoTalk(
    context: Context,
    onEvent: (UiEvent) -> Unit,
) {
    UserApiClient.instance.loginWithKakaoTalk(
        context = context,
        callback = kakaoLoginCallback(onEvent = onEvent)
    )
}

private fun loginWithKakaoAccount(
    context: Context,
    onEvent: (UiEvent) -> Unit,
) {
    UserApiClient.instance.loginWithKakaoAccount(
        context = context,
        callback = kakaoLoginCallback(onEvent = onEvent)
    )
}

private fun navigateToHome(navHostController: NavHostController) {
    navHostController.navigate(Screen.HOME)
}

private fun navigateToParticipationMethod(navHostController: NavHostController) {
    navHostController.navigate(Screen.PARTICIPATION_METHOD)
}

@Composable
private fun OnBoardingScreenContents(
    uiState: OnBoardingUiState,
    onEvent: (UiEvent) -> Unit,
) {
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
            val pagerState = rememberPagerState { PAGE_LIST.size }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FixedHeightPager(
                    pageCount = PAGE_LIST.size,
                    pager = { count, modifier ->
                        HorizontalPager(
                            modifier = Modifier.fillMaxWidth(),
                            state = pagerState
                        ) { pageIndex ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(PAGE_LIST[pageIndex].titleStringId),
                                    style = Head1_B,
                                    textAlign = TextAlign.Center,
                                    color = ColorTextDefault
                                )

                                Text(
                                    modifier = Modifier.padding(top = 8.dp),
                                    text = stringResource(PAGE_LIST[pageIndex].bodyStringId),
                                    style = Body1_R,
                                    textAlign = TextAlign.Center,
                                    color = ColorTextSubtle
                                )

                                Image(
                                    modifier = Modifier.fillMaxWidth(),
                                    painter = painterResource(PAGE_LIST[pageIndex].imageId),
                                    contentScale = ContentScale.FillWidth,
                                    contentDescription = "image_on_boarding"
                                )
                            }
                        }
                    },
                    page = { pageIndex ->
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(PAGE_LIST[pageIndex].titleStringId),
                                style = Head1_B,
                                textAlign = TextAlign.Center,
                                color = ColorTextDefault
                            )
                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                text = stringResource(PAGE_LIST[pageIndex].bodyStringId),
                                style = Body1_R,
                                textAlign = TextAlign.Center,
                                color = ColorTextSubtle
                            )
                            Image(
                                modifier = Modifier.fillMaxWidth(),
                                painter = painterResource(PAGE_LIST[pageIndex].imageId),
                                contentScale = ContentScale.FillWidth,
                                contentDescription = "image_on_boarding"
                            )
                        }
                    }
                )

                PagerIndicator(
                    modifier = Modifier.padding(top = 20.dp),
                    currentPageIndex = pagerState.currentPage,
                    pageCount = pagerState.pageCount
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .height(50.dp)
                .background(ColorKakaoYellow)
                .clickableWithoutRipple { onEvent(OnBoardingUiEvent.Click.OnClickKakaoLogin) },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_kakao),
                tint = ColorKakaoLogo,
                contentDescription = "icon_kakao"
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(R.string.cta_button_kakao_login),
                style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                color = ColorKakaoLabel
            )
        }
    }
}

@Composable
private fun FixedHeightPager(
    pageCount: Int,
    pager: @Composable (Int, Modifier) -> Unit,
    page: @Composable (Int) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val placeables = (0 until pageCount).map { index ->
            val measurables = subcompose("page-$index") { page(index) }
            measurables.maxBy { it.maxIntrinsicHeight(constraints.maxWidth) }.measure(constraints.copy(minHeight = 0))
        }
        val maxHeight = placeables.maxOf { it.height }

        val pagerPlaceables = subcompose("pager") {
            pager(pageCount, Modifier.height(maxHeight.toDp()))
        }.map {
            it.measure(
                constraints.copy(
                    minHeight = maxHeight,
                    maxHeight = maxHeight
                )
            )
        }

        layout(
            width = constraints.maxWidth,
            height = maxHeight
        ) {
            pagerPlaceables.forEach { it.place(0, 0) }
        }
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

@ScreenPreview
@Composable
private fun OnBoardingScreenContentsPreview() {
    OnBoardingScreenContents(
        uiState = OnBoardingUiState(),
        onEvent = {}
    )
}