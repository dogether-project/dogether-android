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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.chottulink.lib.ChottuLink
import site.dogether.presentation.AppNavGraph
import site.dogether.presentation.theme.ColorBgDefault
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.DogetherAndroidTheme
import site.dogether.presentation.utils.LocalDeeplinkInfo
import site.dogether.presentation.utils.LocalNavHostController

class MainActivity : ComponentActivity() {
    private var currentIntent: Intent? by mutableStateOf(null)
    private var deeplinkInfo: String? by mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        currentIntent = intent
        extractDeeplinkInfo(intent)

        setContent {
            DogetherAndroidTheme {
                GlobalComposition {
                    // 딥링크 정보 추출 - currentIntent가 변경될 때마다 실행
                    LaunchedEffect(currentIntent) {
                        currentIntent?.let { intent ->
                            extractDeeplinkInfo(intent)
                        }
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
        CompositionLocalProvider(
            LocalOverscrollFactory provides null,
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = ColorIconPrimary,
                backgroundColor = ColorIconPrimary.copy(alpha = 0.4f)
            ),
            LocalNavHostController provides rememberNavController(),
            LocalDeeplinkInfo provides deeplinkInfo
        ) {
            block()
        }
    }
}