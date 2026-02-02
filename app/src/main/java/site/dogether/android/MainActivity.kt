package site.dogether.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.chottulink.lib.ChottuLink
import site.dogether.android.service.PushType
import site.dogether.common.auth.TokenExpirationManager
import site.dogether.presentation.AppNavGraph
import site.dogether.presentation.R
import site.dogether.presentation.Screen
import site.dogether.presentation.composables.ActionDialog
import site.dogether.presentation.composables.DogetherSnackbar
import site.dogether.presentation.theme.ColorBgDefault
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.DogetherAndroidTheme
import site.dogether.presentation.utils.ErrorCallbackManager
import site.dogether.presentation.utils.LocalDeeplinkInfo
import site.dogether.presentation.utils.LocalErrorCallbackManager
import site.dogether.presentation.utils.LocalNavHostController
import site.dogether.presentation.utils.LocalSnackbarHostState

class MainActivity : ComponentActivity() {
    // TODO : 리팩토링 필요..!
    private var currentIntent: Intent? by mutableStateOf(null)
    private var deeplinkInfo: String? by mutableStateOf(null)
    private var pushRoute: String? by mutableStateOf(null)
    private var showTokenExpiredDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        currentIntent = intent
        extractDeeplinkInfo(intent)

        setContent {
            DogetherAndroidTheme {
                GlobalComposition {
                    val navController = LocalNavHostController.current

                    // 딥링크 정보 추출 - currentIntent가 변경될 때마다 실행
                    LaunchedEffect(currentIntent) {
                        currentIntent?.let { intent ->
                            extractDeeplinkInfo(intent)
                        }
                    }

                    // 푸시 알림으로 인입된 경우 네비게이션 처리
                    LaunchedEffect(pushRoute) {
                        pushRoute?.let { route ->
                            navController.navigate(route) {
                                // 백스택을 HOME까지 유지하고, HOME은 유지
                                popUpTo(Screen.HOME) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                            pushRoute = null // 처리 후 초기화
                        }
                    }

                    // 토큰 만료 이벤트 구독 - 다이얼로그 표시
                    LaunchedEffect(Unit) {
                        TokenExpirationManager.tokenExpiredEvent.collect {
                            showTokenExpiredDialog = true
                        }
                    }

                    // 토큰 만료 다이얼로그
                    if (showTokenExpiredDialog) {
                        ActionDialog(
                            title = "로그인 정보가 만료됐어요",
                            body = "다시 로그인해 주세요.",
                            icon = painterResource(R.drawable.ic_notice),
                            positiveText = "확인",
                            onClickPositive = {
                                showTokenExpiredDialog = false
                                navController.navigate(Screen.ON_BOARDING) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .background(ColorBgDefault)
                    ) {
                        AppNavGraph()
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        currentIntent = intent
        if (intent.extras != null) {
            redirectFromPush(intent)
        }
    }

    // Push로 인입된 유저의 리디렉션
    private fun redirectFromPush(intent: Intent) {
        // Intent에 데이터가 없으면 홈으로 이동
        if (intent.extras == null || intent.extras?.isEmpty == true) {
            pushRoute = Screen.HOME
            return
        }

        val extras = intent.extras ?: return

        when (extras.getString("type")) {
            PushType.JOIN.name -> {
                // 그룹 참여, 다른 그룹원 참여
                pushRoute = Screen.HOME
            }

            PushType.CERTIFICATION.name -> {
                // 내가 다른 그룹원의 투두 검사자로 선정되었을때
                pushRoute = Screen.CHECK_TODO
            }

            PushType.REVIEW.name -> {
                // 검사자가 내 투두 인증을 검사했을 때
                pushRoute = Screen.HOME
            }

            else -> {
                pushRoute = Screen.HOME
            }
        }
    }

    private fun extractDeeplinkInfo(intent: Intent) {
        ChottuLink.getAppLinkData(intent)?.addOnSuccessListener {
            it?.link?.let { link ->
                deeplinkInfo = link.toString()
            } ?: run {
                deeplinkInfo = null
            }
        } ?: run {
            deeplinkInfo = null
        }
    }

    @Composable
    private fun GlobalComposition(block: @Composable () -> Unit) {
        val snackbarHostState = remember { SnackbarHostState() }
        val errorCallbackManager = remember { ErrorCallbackManager() }

        CompositionLocalProvider(
            LocalOverscrollFactory provides null,
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = ColorIconPrimary,
                backgroundColor = ColorIconPrimary.copy(alpha = 0.4f)
            ),
            LocalNavHostController provides rememberNavController(),
            LocalDeeplinkInfo provides deeplinkInfo,
            LocalSnackbarHostState provides snackbarHostState,
            LocalErrorCallbackManager provides errorCallbackManager
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                block()
                // 전역 Snackbar Host
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 30.dp)
                ) { snackbarData ->
                    DogetherSnackbar(
                        message = snackbarData.visuals.message,
                        onDismiss = { snackbarData.dismiss() }
                    )
                }
            }
        }
    }
}