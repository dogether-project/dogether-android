package site.dogether.presentation.screen.participation_method

import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import site.dogether.presentation.R
import site.dogether.presentation.composables.ActionDialog
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorIconElevated
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.theme.Head2_B
import site.dogether.presentation.theme.Yellow
import site.dogether.presentation.utils.ScreenPreview
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

    ParticipationMethodScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )

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
private fun ParticipationMethodScreenContents(
    uiState: ParticipationMethodUiState,
    onEvent: (ParticipationMethodUiEvent) -> Unit,
) {
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
                centerText = stringResource(R.string.title_add_new_group),
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
    onClick: () -> Unit,
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
private fun InitDialog(viewModel: ParticipationMethodViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value

    if (uiState.permissionDialogState.isShowing) {
        ActionDialog(
            title = stringResource(R.string.dialog_title_permission),
            body = stringResource(R.string.dialog_body_permission),
            icon = painterResource(R.drawable.ic_notice),
            negativeText = stringResource(R.string.dialog_button_later),
            positiveText = stringResource(R.string.dialog_button_settings),
            onClickNegative = { viewModel.onEvent(ParticipationMethodUiEvent.Click.OnClickPermissionDialogNegative) },
            onClickPositive = { viewModel.onEvent(ParticipationMethodUiEvent.Click.OnClickPermissionDialogPositive) },
            onDismissRequest = { viewModel.onEvent(ParticipationMethodUiEvent.Callback.OnPermissionDialogDismissRequested) }
        )
    }
}

@ScreenPreview
@Composable
private fun ParticipationMethodScreenContentsPreview() {
    ParticipationMethodScreenContents(
        uiState = ParticipationMethodUiState(),
        onEvent = {}
    )
}