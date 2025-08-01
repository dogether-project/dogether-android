package site.dogether.presentation.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import org.koin.androidx.compose.koinViewModel
import site.dogether.presentation.R
import site.dogether.presentation.utils.LifecycleEvent

@Composable
fun SplashScreen(viewModel: SplashViewModel = koinViewModel()) {
    LifecycleEvent(Lifecycle.Event.ON_START) {
        viewModel.onEvent(SplashUiEvent.Lifecycle.OnStart)
    }

    SplashScreenContents()
}

@Composable
private fun SplashScreenContents() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(R.drawable.ic_splash),
            contentDescription = "icon_splash"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreenContents()
}