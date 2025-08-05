package site.dogether.presentation.screen.participate_group

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.DogetherTextField
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B

@Composable
fun ParticipateGroupScreen(viewModel: ParticipateGroupViewModel = koinViewModel()) {
    ParticipateGroupScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

@Composable
private fun ParticipateGroupScreenContents(
    uiState: ParticipateGroupUiState,
    onEvent: (ParticipateGroupUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                start = {
                    BackButton {}
                },
                centerText = stringResource(R.string.title_participate_group)
            )

            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = stringResource(R.string.title_type_invite_code),
                style = Head1_B,
                color = ColorTextDefault
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.body_type_invite_code),
                style = Body1_R,
                color = ColorTextSubtle
            )

            DogetherTextField(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                value = uiState.inviteCode,
                onValueChanged = { text -> onEvent(ParticipateGroupUiEvent.Type.OnInviteCodeTyped(text)) },
                hintText = stringResource(R.string.input_hint_type_invite_code)
            )
        }

        CTAButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(50.dp),
            text = stringResource(R.string.cta_button_participate),
            radius = 8.dp,
            isEnabled = uiState.isValid,
            onClick = { }
        )
    }
}

@Preview()
@Composable
private fun ParticipateGroupScreenContentsPreview() {
    ParticipateGroupScreenContents(
        uiState = ParticipateGroupUiState(),
        onEvent = {}
    )
}