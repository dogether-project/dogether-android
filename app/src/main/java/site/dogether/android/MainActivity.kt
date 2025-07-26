package site.dogether.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsControllerCompat
import site.dogether.presentation.AppNavGraph
import site.dogether.presentation.theme.DogetherAndroidTheme
import site.dogether.presentation.theme.Grey900

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = false

        setContent {
            DogetherAndroidTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Grey900)
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    AppNavGraph()
                }
            }
        }
    }
}