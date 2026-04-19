package site.dogether.presentation.screen.on_boarding

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.Screen
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.LoadingDialog
import site.dogether.presentation.composables.node.throttledClickable
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

private val PAGE_LIST: List<OnBoardingPage> = OnBoardingPage.entries

@Composable
fun OnBoardingScreen(viewModel: OnBoardingViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value
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

            is OnBoardingUiEffect.NavigateToParticipateGroup -> {
                navHostController.navigate("${Screen.PARTICIPATE_GROUP}/${uiEffect.joinCode}")
            }
        }
    }

    OnBoardingScreenContents(
        uiState = uiState,
        onEvent = onEvent
    )

    InitDialog(uiState)
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
    navHostController.navigate(Screen.HOME) {
        popUpTo(0) { inclusive = false }
    }
}

@Composable
private fun OnBoardingScreenContents(
    uiState: OnBoardingUiState,
    onEvent: (UiEvent) -> Unit,
) {
    val composition0 by rememberLottieComposition(LottieCompositionSpec.RawRes(PAGE_LIST[0].lottieResId))
    val composition1 by rememberLottieComposition(LottieCompositionSpec.RawRes(PAGE_LIST[1].lottieResId))
    val composition2 by rememberLottieComposition(LottieCompositionSpec.RawRes(PAGE_LIST[2].lottieResId))
    val compositions = listOf(composition0, composition1, composition2)

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
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    val pagerState = rememberPagerState { PAGE_LIST.size }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FixedHeightPager(
                            pageCount = PAGE_LIST.size,
                            pageContent = { pageIndex ->
                                OnBoardingPageContent(
                                    page = PAGE_LIST[pageIndex],
                                    composition = compositions[pageIndex]
                                )
                            },
                            pagerState = pagerState
                        )

                        PagerIndicator(
                            modifier = Modifier.padding(top = 20.dp),
                            currentPageIndex = pagerState.currentPage,
                            pageCount = pagerState.pageCount
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .height(50.dp)
                .background(ColorKakaoYellow)
                .throttledClickable { onEvent(OnBoardingUiEvent.Click.OnClickKakaoLogin) },
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
private fun InitDialog(uiState: OnBoardingUiState) {
    if (uiState.isLoading) {
        LoadingDialog()
    }
}

@Composable
private fun OnBoardingPageContent(
    page: OnBoardingPage,
    composition: com.airbnb.lottie.LottieComposition?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(page.titleStringId),
            style = Head1_B,
            textAlign = TextAlign.Center,
            color = ColorTextDefault
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(page.bodyStringId),
            style = Body1_R,
            textAlign = TextAlign.Center,
            color = ColorTextSubtle
        )

        LottieAnimation(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun FixedHeightPager(
    pageCount: Int,
    pageContent: @Composable (Int) -> Unit,
    pagerState: PagerState
) {
    SubcomposeLayout(modifier = Modifier.fillMaxWidth()) { constraints ->
        val measuredHeights = (0 until pageCount).map { index ->
            subcompose(index) { pageContent(index) }.first().measure(constraints).height
        }
        val maxHeight = measuredHeights.maxOrNull() ?: 0

        val pagerPlaceable = subcompose("pager") {
            HorizontalPager(
                modifier = Modifier.height(maxHeight.toDp()),
                state = pagerState
            ) { pageIndex ->
                pageContent(pageIndex)
            }
        }.first().measure(
            constraints.copy(
                minHeight = maxHeight,
                maxHeight = maxHeight
            )
        )

        layout(pagerPlaceable.width, pagerPlaceable.height) {
            pagerPlaceable.placeRelative(0, 0)
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