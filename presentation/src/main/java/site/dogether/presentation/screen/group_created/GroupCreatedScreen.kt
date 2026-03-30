package site.dogether.presentation.screen.group_created

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.chottulink.lib.ChottuLink
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.common.utils.DeeplinkUtil
import site.dogether.presentation.R
import site.dogether.presentation.Screen
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.node.throttledClickable
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorBorderDisabled
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorIconSecondary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.LocalNavHostController
import site.dogether.presentation.utils.ScreenPreview

@Composable
fun GroupCreatedScreen(viewModel: GroupCreatedViewModel = koinViewModel()) {
    val context = LocalContext.current
    val navController = LocalNavHostController.current

    viewModel.CollectEffect<GroupCreatedUiEffect> { uiEffect ->
        when (uiEffect) {
            is GroupCreatedUiEffect.ShareJoinCode -> shareJoinCode(
                context = context,
                joinCode = uiEffect.joinCode
            )
        }
    }

    GroupCreatedScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )

    BackHandler {
        navController.navigate(Screen.PARTICIPATION_METHOD) {
            popUpTo(Screen.PARTICIPATION_METHOD) {
                inclusive = true
            }
        }
    }
}

private fun shareJoinCode(context: Context, joinCode: String) {
    ChottuLink.createDynamicLink()
        .setLink(DeeplinkUtil.generateInviteDeeplink(joinCode).toUri())
        .setDomain(DeeplinkUtil.DEEPLINK_DOMAIN)
        .build()
        .addOnSuccessListener {
            it?.let { result ->
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        context.getString(R.string.intent_title_share_join_code)
                    )
                    putExtra(Intent.EXTRA_TEXT, result.uri.toString())
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(
                    sendIntent,
                    context.getString(R.string.intent_title_share_join_code)
                )
                context.startActivity(shareIntent)
            }
        }
}

@Composable
private fun GroupCreatedScreenContents(
    uiState: GroupCreatedUiState,
    onEvent: (UiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(top = 68.dp)
                .fillMaxWidth()
                .statusBarsPadding()
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
                text = stringResource(R.string.title_group_created),
                style = Head1_B,
                textAlign = TextAlign.Center,
                color = ColorTextDefault
            )

            Box(
                modifier = Modifier
                    .padding(
                        top = 68.dp,
                        start = (28.5).dp,
                        end = (28.5).dp
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .height(84.dp)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(12.dp),
                        color = ColorBorderDisabled
                    )
                    .background(ColorBgSurface)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uiState.joinCode,
                        style = Head1_B.copy(lineHeightStyle = LineHeightStyle.Default),
                        color = ColorTextDefault
                    )

                    Icon(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .minimumInteractiveComponentSize()
                            .throttledClickable { onEvent(GroupCreatedUiEvent.Click.OnClickShare) },
                        painter = painterResource(R.drawable.ic_export),
                        tint = ColorIconSecondary,
                        contentDescription = "icon_export"
                    )
                }
            }

            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.ic_notice_empty),
                    tint = ColorIconSecondary,
                    contentDescription = "icon_notice_empty"
                )

                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(R.string.notice_share),
                    style = Body2_R.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextSecondary
                )
            }
        }

        CTAButton(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(50.dp),
            isEnabled = true,
            text = stringResource(R.string.cta_button_navigate_to_home),
            onClick = { onEvent(GroupCreatedUiEvent.Click.OnClickNavigateToHome) }
        )
    }
}

@ScreenPreview
@Composable
private fun GroupCreatedScreenContentsPreview() {
    GroupCreatedScreenContents(
        uiState = GroupCreatedUiState(),
        onEvent = {}
    )
}