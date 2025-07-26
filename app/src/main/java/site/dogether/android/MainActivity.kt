package site.dogether.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import site.dogether.presentation.theme.DogetherAndroidTheme
import site.dogether.presentation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogetherAndroidTheme {
                AppNavGraph()
            }
        }
    }
}