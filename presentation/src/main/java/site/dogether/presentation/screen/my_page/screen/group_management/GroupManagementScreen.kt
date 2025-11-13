package site.dogether.presentation.screen.my_page.screen.group_management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.domain.model.group.Group
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.ActionDialog
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.theme.Body2_S
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.Head2_B
import site.dogether.presentation.theme.Red400
import site.dogether.presentation.theme.Small_R
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.clickableWithoutRipple

@Composable
fun GroupManagementScreen(viewModel: GroupManagementViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value

    viewModel.CollectEffect<GroupManagementUiEffect> { uiEffect ->
        when (uiEffect) {
            else -> Unit
        }
    }

    GroupManagementScreenContents(
        uiState = uiState,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )

    InitDialog(
        uiState = uiState,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

@Composable
private fun GroupManagementScreenContents(
    uiState: GroupManagementUiState,
    onEvent: (UiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        TopBar(
            start = { BackButton { onEvent(UiEvent.Click.OnClickBack) } },
            centerText = stringResource(R.string.title_group_management)
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.groups) { group ->
                GroupItem(
                    group = group,
                    onClickWithdraw = { groupId -> onEvent(GroupManagementUiEvent.Click.OnClickWithdraw(groupId)) }
                )
            }
        }
    }
}

@Composable
private fun InitDialog(
    uiState: GroupManagementUiState,
    onEvent: (UiEvent) -> Unit,
) {
    if (uiState.withdrawGroupDialogState.isShowing) {
        ActionDialog(
            title = stringResource(R.string.dialog_title_withdraw_group),
            body = stringResource(R.string.dialog_body_withdraw_group),
            positiveButtonColor = Red400,
            negativeText = stringResource(R.string.dialog_button_back),
            positiveText = stringResource(R.string.dialog_button_withdraw),
            onClickNegative = { onEvent(GroupManagementUiEvent.Click.OnClickWithdrawDialogNegative) },
            onClickPositive = { onEvent(GroupManagementUiEvent.Click.OnClickWithdrawDialogPositive) },
            onDismissRequest = { onEvent(GroupManagementUiEvent.Callback.OnWithdrawDialogDismissRequested) }
        )
    }
}

@Composable
private fun GroupItem(
    group: Group,
    onClickWithdraw: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(ColorBgElevated)
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = group.name,
                style = Head2_B.copy(lineHeightStyle = LineHeightStyle.Default),
                color = ColorTextDefault
            )

            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ColorBgSurface)
                    .padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    )
                    .clickableWithoutRipple { onClickWithdraw(group.id) },
                text = stringResource(R.string.cta_button_withdraw),
                style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default.copy(trim = LineHeightStyle.Trim.None)),
                color = ColorTextDefault
            )
        }

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.info_group_member_count),
                style = Small_R,
                color = ColorTextSecondary
            )

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "${group.currentMemberCount}/${group.maximumMemberCount}",
                style = Small_R,
                color = ColorTextDefault
            )

            Text(
                modifier = Modifier.padding(start = 18.dp),
                text = stringResource(R.string.info_end_date),
                style = Small_R,
                color = ColorTextSecondary
            )

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = group.endAt,
                style = Small_R,
                color = ColorTextDefault
            )
        }

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.info_join_code),
                style = Small_R,
                color = ColorTextSecondary
            )

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = group.joinCode,
                style = Small_R,
                color = ColorTextDefault
            )
        }
    }
}

@ScreenPreview
@Composable
private fun GroupManagementScreenContentsPreview() {
    GroupManagementScreenContents(
        uiState = GroupManagementUiState(),
        onEvent = {}
    )
}