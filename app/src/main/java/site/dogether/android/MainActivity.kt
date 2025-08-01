package site.dogether.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsControllerCompat
import site.dogether.presentation.AppNavGraph
import site.dogether.presentation.theme.ColorBgDefault
import site.dogether.presentation.theme.DogetherAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = false

        setContent {
            DogetherAndroidTheme {
                GlobalComposition {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ColorBgDefault)
                            .statusBarsPadding()
                            .navigationBarsPadding()
                    ) {
                        AppNavGraph()
                    }
                }
            }
        }
    }

    @Composable
    private fun GlobalComposition(block: @Composable () -> Unit) {
        CompositionLocalProvider(LocalOverscrollFactory.provides(null)) {
            block()
        }
    }
}