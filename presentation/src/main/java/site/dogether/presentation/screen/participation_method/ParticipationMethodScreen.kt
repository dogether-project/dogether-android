package site.dogether.presentation.screen.participation_method

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.Screen
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.BackButton
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
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.LocalNavHostController
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.clickableWithoutRipple
import site.dogether.presentation.utils.intervaledClickableWithoutRipple

@Composable
fun ParticipationMethodScreen(viewModel: ParticipationMethodViewModel = koinViewModel()) {
    val navHostController = LocalNavHostController.current

    viewModel.CollectEffect<ParticipationMethodUiEffect> { uiEffect ->
        when (uiEffect) {
            is ParticipationMethodUiEffect.NavigateToCreateGroup -> navigateToCreateGroup(navHostController)

            is ParticipationMethodUiEffect.NavigateToParticipateGroup -> navigateToParticipateWithCode(navHostController)
        }
    }

    ParticipationMethodScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

private fun navigateToCreateGroup(navHostController: NavHostController) {
    navHostController.navigate(Screen.CREATE_GROUP)
}

private fun navigateToParticipateWithCode(navHostController: NavHostController) {
    navHostController.navigate(Screen.PARTICIPATE_GROUP)
}

@Composable
private fun ParticipationMethodScreenContents(
    uiState: ParticipationMethodUiState,
    onEvent: (UiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        if (uiState.isParticipatingGroupExist) {
            TopBar(
                start = { BackButton {} },
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
                        modifier = Modifier.clickableWithoutRipple { onEvent(ParticipationMethodUiEvent.Click.OnClickMyPage) },
                        painter = painterResource(R.drawable.ic_my),
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
            onClick = { onEvent(ParticipationMethodUiEvent.Click.OnClickCreateGroup) }
        )

        ParticipationMethod(
            modifier = Modifier.padding(top = 16.dp),
            icon = painterResource(R.drawable.ic_key),
            iconTint = Yellow,
            iconContentDescription = "icon_key",
            title = stringResource(R.string.cta_button_title_participate_with_code),
            body = stringResource(R.string.cta_button_body_participate_with_code),
            onClick = { onEvent(ParticipationMethodUiEvent.Click.OnClickParticipateGroup) }
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
            .intervaledClickableWithoutRipple { onClick() },
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

@ScreenPreview
@Composable
private fun ParticipationMethodScreenContentsPreview() {
    ParticipationMethodScreenContents(
        uiState = ParticipationMethodUiState(),
        onEvent = {}
    )
}