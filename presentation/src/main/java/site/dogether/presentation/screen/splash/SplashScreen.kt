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

/*
val context = LocalContext.current

viewModel.collectSideEffect { uiEffect ->
    when (uiEffect) {
        is SplashUiEffect.CheckNotificationPermission -> checkNotificationPermission(
            context = context,
            onGranted = { viewModel.onPermissionGranted() },
            onDenied = { viewModel.onPermissionDenied() }
        )

        is SplashUiEffect.NavigateToNotificationSetting -> navigateToNotificationSetting(context)
    }
}

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    LifecycleEvent(Lifecycle.Event.ON_START) {
        viewModel.onStarted()
    }
}

@SuppressLint("InlinedApi")
private fun checkNotificationPermission(
    context: Context,
    onGranted: () -> Unit,
    onDenied: () -> Unit,
) {
    if (context.isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) {
        onGranted()
    } else {
        onDenied()
    }
}

private fun navigateToNotificationSetting(context: Context) {
    context.startActivity(
        Intent().apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
    )
}

@Composable
private fun InitDialog() {
    val uiState = viewModel<SplashViewModel>().collectAsState().value

    if (uiState.isPermissionDialogShowing) {
        PermissionDialog()
    }
}

@Composable
private fun PermissionDialog() {
    val viewModel = viewModel<SplashViewModel>()

    Dialog(
        onDismissRequest = { viewModel.onPermissionDialogDismissRequested() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .background(ColorBgSurface)
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 32.dp,
                        bottom = 24.dp
                    )
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_notice),
                    tint = ColorIconPrimary,
                    contentDescription = "icon_notice"
                )

                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = stringResource(R.string.dialog_title_permission),
                    style = Head1_B,
                    color = ColorTextDefault
                )

                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.dialog_body_permission),
                    style = Body1_R,
                    color = ColorTextSubtle,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                ) {
                    NegativeCTAButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        text = stringResource(R.string.dialog_button_later),
                        radius = 8.dp,
                        onClick = { viewModel.onPermissionDialogDismissRequested() }
                    )

                    Spacer(modifier = Modifier.width(11.dp))

                    CTAButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        text = stringResource(R.string.dialog_button_settings),
                        isEnabled = true,
                        radius = 8.dp,
                        onClick = { viewModel.onClickNavigateToNotificationSetting() }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PermissionDialogPreview() {
    PermissionDialog()
}

 */

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreenContents()
}