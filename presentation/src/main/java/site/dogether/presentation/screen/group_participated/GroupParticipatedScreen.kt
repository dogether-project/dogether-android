package site.dogether.presentation.screen.group_participated

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.GroupInfoBoard
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.utils.ScreenPreview

@Composable
fun GroupParticipatedScreen(viewModel: GroupParticipatedViewModel = koinViewModel()) {
    GroupParticipatedScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

@Composable
private fun GroupParticipatedScreenContents(
    uiState: GroupParticipatedUiState,
    onEvent: (GroupParticipatedUiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(top = 68.dp)
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_congrats),
                tint = ColorIconPrimary,
                contentDescription = "icon_congrats"
            )

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(R.string.title_group_participated),
                style = Head1_B,
                textAlign = TextAlign.Center,
                color = ColorTextDefault
            )

            GroupInfoBoard(
                modifier = Modifier.padding(
                    top = 40.dp,
                    start = 32.dp,
                    end = 32.dp
                ),
                name = "name",
                duration = 14,
                maximumMemberCount = 10,
                isLaunchFromToday = true
            )
        }

        CTAButton(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(50.dp),
            isEnabled = true,
            text = stringResource(R.string.cta_button_navigate_to_home),
            onClick = {}
        )
    }
}

@ScreenPreview
@Composable
private fun GroupParticipatedScreenContentsPreview() {
    GroupParticipatedScreenContents(
        uiState = GroupParticipatedUiState(),
        onEvent = {}
    )
}