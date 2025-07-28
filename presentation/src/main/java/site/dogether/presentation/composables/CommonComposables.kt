package site.dogether.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import site.dogether.presentation.R
import site.dogether.presentation.theme.Blue300
import site.dogether.presentation.theme.Body1_B
import site.dogether.presentation.theme.Grey300
import site.dogether.presentation.theme.Grey400
import site.dogether.presentation.theme.Grey500
import site.dogether.presentation.theme.Grey800
import site.dogether.presentation.utils.clickableWithoutRipple
import site.dogether.presentation.utils.conditionedClickableWithoutRipple

@Composable
fun CTAButton(
    modifier: Modifier,
    isEnabled: Boolean = true,
    radius: Dp = 12.dp,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.then(
            Modifier
                .clip(RoundedCornerShape(radius))
                .background(if (isEnabled) Blue300 else Grey500)
                .conditionedClickableWithoutRipple(isEnabled) { onClick() })
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = Body1_B,
            color = if (isEnabled) Grey800 else Grey400
        )
    }
}

@Preview
@Composable
private fun CTAButtonPreview() {
    CTAButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        isEnabled = false,
        text = stringResource(R.string.dialog_button_settings),
        onClick = {}
    )
}

@Composable
fun NegativeCTAButton(
    modifier: Modifier,
    text: String,
    radius: Dp = 12.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.then(
            Modifier
                .clip(RoundedCornerShape(radius))
                .background(Transparent)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(radius),
                    color = Grey500
                )
                .clickableWithoutRipple { onClick() })
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = Body1_B,
            color = Grey300
        )
    }
}

@Preview
@Composable
private fun NegativeCTAButtonPreview() {
    NegativeCTAButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        text = stringResource(R.string.dialog_button_later),
        onClick = {}
    )
}
