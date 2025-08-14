package site.dogether.presentation.screen.error

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.NegativeCTAButton
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.screen.error.model.Error
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head2_B
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.clickableWithoutRipple

@Composable
fun ErrorScreen(viewModel: ErrorViewModel = koinViewModel()) {
    ErrorScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

@Composable
private fun ErrorScreenContents(
    uiState: ErrorUiState,
    onEvent: (ErrorUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            end = {
                Icon(
                    modifier = Modifier.clickableWithoutRipple {},
                    painter = painterResource(R.drawable.ic_close),
                    tint = ColorIconDefault,
                    contentDescription = "icon_close"
                )
            }
        )

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            uiState.error?.let { error ->
                Image(
                    modifier = Modifier.size(200.dp),
                    painter = painterResource(
                        when (error) {
                            Error.Network -> R.drawable.img_error_network
                            Error.Unknown -> R.drawable.img_error_unknown
                            Error.LoadData -> R.drawable.img_error_load_data
                            else -> R.drawable.img_error_common
                        }
                    ),
                    contentDescription = "image_error"
                )

                Text(
                    modifier = Modifier.padding(top = 32.dp),
                    text = stringResource(error.titleStringId),
                    style = Head2_B,
                    color = ColorTextSubtle
                )

                error.bodyStringId?.let { bodyStringId ->
                    Text(
                        text = stringResource(bodyStringId),
                        style = Body2_R,
                        color = ColorTextSecondary
                    )
                }

                Row(
                    modifier = Modifier.padding(
                        top = 20.dp,
                        start = 36.dp,
                        end = 36.dp
                    )
                ) {
                    if (error.negativeButtonStringId == null) {
                        CTAButton(
                            modifier = Modifier
                                .width(154.dp)
                                .height(50.dp),
                            radius = 8.dp,
                            text = stringResource(error.positiveButtonStringId),
                            onClick = {}
                        )
                    } else {
                        NegativeCTAButton(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            radius = 8.dp,
                            text = stringResource(error.negativeButtonStringId),
                            onClick = {}
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        CTAButton(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            radius = 8.dp,
                            text = stringResource(error.positiveButtonStringId),
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}

@ScreenPreview
@Composable
private fun ErrorScreenContentsPreview() {
    ErrorScreenContents(
        uiState = ErrorUiState(),
        onEvent = {}
    )
}