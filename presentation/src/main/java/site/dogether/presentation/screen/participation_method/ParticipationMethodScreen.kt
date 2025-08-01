package site.dogether.presentation.screen.participation_method

import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import site.dogether.presentation.R
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.NegativeCTAButton
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorIconElevated
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.theme.Head2_B
import site.dogether.presentation.theme.Yellow
import site.dogether.presentation.utils.clickableWithoutRipple
import site.dogether.presentation.utils.isPermissionGranted

@Composable
fun ParticipationMethodScreen(viewModel: ParticipationMethodViewModel = koinViewModel()) {
    val context = LocalContext.current

    viewModel.collectSideEffect { uiEffect ->
        when (uiEffect) {
            is ParticipationMethodUiEffect.NavigateToCreateGroup -> navigateToCreateGroup()

            is ParticipationMethodUiEffect.NavigateToParticipateWithCode -> navigateToParticipateWithCode()

            is ParticipationMethodUiEffect.CheckNotificationPermission -> checkNotificationPermission(
                context = context,
                onDenied = { viewModel.onEvent(ParticipationMethodUiEvent.Callback.OnPermissionDenied) }
            )

            is ParticipationMethodUiEffect.NavigateToNotificationSetting -> navigateToNotificationSetting(context)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(ParticipationMethodUiEvent.Lifecycle.OnFirstComposition)
    }

    ParticipationMethodScreenContents()

    InitDialog()
}

private fun navigateToCreateGroup() = Unit

private fun navigateToParticipateWithCode() = Unit

private fun checkNotificationPermission(
    context: Context,
    onDenied: () -> Unit,
) {
    if (!context.isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) onDenied()
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
private fun ParticipationMethodScreenContents(viewModel: ParticipationMethodViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        if (uiState.isParticipatingGroupExist) {
            TopBar(
                start = {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        tint = ColorIconDefault,
                        contentDescription = "icon_arrow_back"
                    )
                },
                center = {
                    Text(
                        text = stringResource(R.string.title_add_new_group),
                        style = Head2_B,
                        color = ColorTextDefault,
                    )
                }
            )
        } else {
            TopBar(
                start = {
                    Icon(
                        painter = painterResource(R.drawable.ic_logo_text_small),
                        tint = ColorIconDefault,
                        contentDescription = "icon_logo_text_small"
                    )
                },
                end = {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_forward),
                        tint = ColorIconDefault,
                        contentDescription = "icon_arrow_forward"
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (!uiState.isParticipatingGroupExist) {
            Text(
                text = stringResource(R.string.body_add_new_group),
                style = Head1_B,
                color = ColorTextDefault
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

        ParticipationMethod(
            icon = painterResource(R.drawable.ic_create_group),
            iconTint = ColorIconPrimary,
            iconContentDescription = "icon_create_group",
            title = stringResource(R.string.cta_button_title_create_group),
            body = stringResource(R.string.cta_button_body_create_group),
            onClick = {}
        )

        ParticipationMethod(
            modifier = Modifier.padding(top = 16.dp),
            icon = painterResource(R.drawable.ic_key),
            iconTint = Yellow,
            iconContentDescription = "icon_key",
            title = stringResource(R.string.cta_button_title_participate_with_code),
            body = stringResource(R.string.cta_button_body_participate_with_code),
            onClick = {}
        )
    }
}

@Composable
private fun ParticipationMethod(
    modifier: Modifier = Modifier,
    icon: Painter,
    iconTint: Color,
    iconContentDescription: String,
    title: String,
    body: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .height(100.dp)
            .background(ColorBgElevated)
            .padding(horizontal = 16.dp)
            .clickableWithoutRipple { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = icon,
                    tint = iconTint,
                    contentDescription = iconContentDescription
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = title,
                    style = Head2_B,
                    color = ColorTextDefault
                )
            }

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = body,
                style = Body2_R,
                color = ColorTextSecondary
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_brace_right),
            tint = ColorIconElevated,
            contentDescription = "icon_brace_right"
        )
    }
}

@Composable
private fun InitDialog() {
    val uiState = koinViewModel<ParticipationMethodViewModel>().collectAsState().value

    if (uiState.permissionDialogState.isShowing) {
        PermissionDialog()
    }
}

@Composable
private fun PermissionDialog(viewModel: ParticipationMethodViewModel = koinViewModel()) {
    Dialog(
        onDismissRequest = { viewModel.onEvent(ParticipationMethodUiEvent.Callback.OnPermissionDialogDismissRequested) },
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
                        horizontal = 20.dp,
                        vertical = 24.dp
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
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                ) {
                    NegativeCTAButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        text = stringResource(R.string.dialog_button_later),
                        radius = 8.dp,
                        onClick = { viewModel.onEvent(ParticipationMethodUiEvent.Click.OnClickPermissionDialogNegative) }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    CTAButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        text = stringResource(R.string.dialog_button_settings),
                        isEnabled = true,
                        radius = 8.dp,
                        onClick = { viewModel.onEvent(ParticipationMethodUiEvent.Click.OnClickPermissionDialogPositive) }
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

@Preview(showBackground = true)
@Composable
private fun ParticipationMethodScreenContentsPreview() {
    ParticipationMethodScreenContents()
}