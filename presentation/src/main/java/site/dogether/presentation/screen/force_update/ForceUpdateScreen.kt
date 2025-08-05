package site.dogether.presentation.screen.force_update

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import site.dogether.presentation.R
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B

@Composable
fun ForceUpdateScreen(viewModel: ForceUpdateViewModel = koinViewModel()) {
    ForceUpdateScreenContents { uiEvent ->
        viewModel.onEvent(uiEvent)
    }
}

@Composable
private fun ForceUpdateScreenContents(onEvent: (ForceUpdateUiEvent) -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_logo_text),
                    contentDescription = "ic_logo_text"
                )

                Text(
                    modifier = Modifier.padding(top = 40.dp),
                    text = stringResource(R.string.title_force_update),
                    style = Head1_B,
                    color = ColorTextDefault
                )

                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.body_force_update),
                    style = Body1_R,
                    color = ColorTextSubtle,
                    textAlign = TextAlign.Center
                )

                Image(
                    modifier = Modifier.padding(top = 44.dp),
                    painter = painterResource(R.drawable.img_party_dosik),
                    contentDescription = "image_party_dosik"
                )
            }
        }

        CTAButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(50.dp),
            radius = 8.dp,
            text = stringResource(R.string.cta_button_force_update),
            onClick = { onEvent(ForceUpdateUiEvent.Click.OnClickUpdate) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ForceUpdateScreenContentsPreview() {
    ForceUpdateScreenContents {}
}