package site.dogether.presentation.screen.todo.certificate

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.BottomEndCounterDogetherTextField
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.LoadingDialog
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.LifecycleEvent
import site.dogether.presentation.utils.LocalNavHostController

@Composable
fun CertificateDescriptionScreen(
    viewModel: CertificateDescriptionViewModel = koinViewModel()
) {
    val uiState = viewModel.collectAsState().value
    val navController = LocalNavHostController.current
    val context = LocalContext.current

    LifecycleEvent(Lifecycle.Event.ON_START) {
        viewModel.onEvent(CertificateDescriptionUiEvent.Lifecycle.OnStart)
    }

    viewModel.CollectEffect<CertificateDescriptionUiEffect> { uiEffect ->
        when (uiEffect) {
            is CertificateDescriptionUiEffect.Back -> {
                navController.popBackStack()
            }

            is CertificateDescriptionUiEffect.NavigateToNext -> {
                navController.popBackStack()
            }

            is CertificateDescriptionUiEffect.NavigateToHome -> {
                // 홈으로 이동 (인증 플로우 완료)
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
    }

    CertificateDescriptionScreenContents(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        context = context
    )

    InitDialog(uiState)
}

@Composable
private fun CertificateDescriptionScreenContents(
    uiState: CertificateDescriptionUiState = CertificateDescriptionUiState(),
    onEvent: (UiEvent) -> Unit = {},
    context: Context = LocalContext.current
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        TopBar(
            start = { BackButton { onEvent(UiEvent.Click.OnClickBack) } },
            centerText = context.getString(R.string.certificate_description_screen_title)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = context.getString(R.string.certificate_description_title),
            style = Head1_B.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorTextDefault,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.ic_caution),
                contentDescription = "icon_caution"
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = context.getString(R.string.certificate_description_warning),
                style = Body2_R.copy(lineHeightStyle = LineHeightStyle.Default),
                color = ColorTextSecondary,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // 설명 입력 필드
        BottomEndCounterDogetherTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            value = uiState.description,
            textStyle = Body1_S.copy(textAlign = TextAlign.Start),
            hintTextStyle = Body1_S.copy(textAlign = TextAlign.Start),
            onValueChanged = { text -> onEvent(CertificateDescriptionUiEvent.UpdateDescription(text)) },
            hintText = context.getString(R.string.certificate_description_hint),
            lengthLimit = 40,
            singleLine = false
        )

        Spacer(modifier = Modifier.weight(1f))

        // 인증하기 버튼
        CTAButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            isEnabled = uiState.description.isNotBlank(),
            text = context.getString(R.string.certificate_description_submit_button),
            onClick = { onEvent(CertificateDescriptionUiEvent.SubmitCertificate) }
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun InitDialog(uiState: CertificateDescriptionUiState) {
    if (uiState.isLoading) {
        LoadingDialog()
    }
}

@Preview
@Composable
fun PreviewCertificateDescriptionScreen() {
    CertificateDescriptionScreenContents()
}
