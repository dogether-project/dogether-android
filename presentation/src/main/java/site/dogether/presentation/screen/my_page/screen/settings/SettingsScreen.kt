package site.dogether.presentation.screen.my_page.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.ActionDialog
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.theme.Body1_B
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.ColorIconElevated
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.Red400
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.clickableWithoutRipple

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value

    SettingsScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )

    InitDialog(
        uiState = uiState,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

@Composable
private fun SettingsScreenContents(
    uiState: SettingsUiState,
    onEvent: (UiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        TopBar(
            start = { BackButton {} },
            centerText = stringResource(R.string.title_settings)
        )

        Column(modifier = Modifier.padding(top = 4.dp)) {
            SettingsMenuItem(
                title = stringResource(R.string.settings_menu_logout),
                onClick = { onEvent(SettingsUiEvent.Click.OnClickLogout) },
                endBlock = {
                    Icon(
                        painter = painterResource(R.drawable.ic_brace_right),
                        tint = ColorIconElevated,
                        contentDescription = "icon_brace_right"
                    )
                }
            )

            SettingsMenuItem(
                title = stringResource(R.string.settings_menu_withdraw),
                onClick = { onEvent(SettingsUiEvent.Click.OnClickWithdraw) },
                endBlock = {
                    Icon(
                        painter = painterResource(R.drawable.ic_brace_right),
                        tint = ColorIconElevated,
                        contentDescription = "icon_brace_right"
                    )
                }
            )

            SettingsMenuItem(
                title = stringResource(R.string.settings_menu_app_version),
                onClick = {},
                endBlock = {
                    Text(
                        text = "1.0.0",
                        style = Body1_B.copy(lineHeightStyle = LineHeightStyle.Default),
                        color = ColorTextDefault
                    )
                }
            )
        }
    }
}

@Composable
private fun InitDialog(
    uiState: SettingsUiState,
    onEvent: (UiEvent) -> Unit,
) {
    if (uiState.logoutDialogState.isShowing) {
        ActionDialog(
            title = stringResource(R.string.dialog_title_logout),
            body = "",
            negativeText = stringResource(R.string.dialog_button_back),
            positiveText = stringResource(R.string.dialog_button_logout),
            onClickNegative = { onEvent(SettingsUiEvent.Click.OnClickLogoutDialogNegative) },
            onClickPositive = { onEvent(SettingsUiEvent.Click.OnClickLogoutDialogPositive) },
            onDismissRequest = { onEvent(SettingsUiEvent.Callback.OnLogoutDialogDismissRequested) },
        )
    }

    if (uiState.withdrawDialogState.isShowing) {
        ActionDialog(
            title = stringResource(R.string.dialog_title_withdraw),
            body = stringResource(R.string.dialog_body_withdraw),
            negativeText = stringResource(R.string.dialog_button_back),
            positiveText = stringResource(R.string.dialog_button_withdraw),
            positiveButtonColor = Red400,
            onClickNegative = { onEvent(SettingsUiEvent.Click.OnClickWithdrawDialogNegative) },
            onClickPositive = { onEvent(SettingsUiEvent.Click.OnClickWithdrawDialogPositive) },
            onDismissRequest = { onEvent(SettingsUiEvent.Callback.OnWithdrawDialogDismissRequested) }
        )
    }
}

@Composable
private fun SettingsMenuItem(
    title: String,
    onClick: () -> Unit,
    endBlock: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickableWithoutRipple { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = Body1_R.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorTextDefault
        )

        endBlock()
    }
}

@ScreenPreview
@Composable
private fun SettingsScreenContentsPreview() {
    SettingsScreenContents(
        uiState = SettingsUiState(),
        onEvent = {}
    )
}