package site.dogether.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import site.dogether.presentation.R
import site.dogether.presentation.theme.Body1_B
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.ColorBgDisabled
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBgPrimary
import site.dogether.presentation.theme.ColorBorderPrimary
import site.dogether.presentation.theme.ColorTextBlack
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextDisabled
import site.dogether.presentation.theme.ColorTextPrimary
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head2_B
import site.dogether.presentation.theme.Small_S
import site.dogether.presentation.utils.clickableWithoutRipple
import site.dogether.presentation.utils.conditionedClickableWithoutRipple

@Composable
fun CTAButton(
    modifier: Modifier,
    isEnabled: Boolean = true,
    radius: Dp = 12.dp,
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.then(
            Modifier
                .clip(RoundedCornerShape(radius))
                .background(if (isEnabled) ColorBgPrimary else ColorBgDisabled)
                .conditionedClickableWithoutRipple(isEnabled) { onClick() })
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = Body1_B.copy(lineHeightStyle = LineHeightStyle.Default),
            color = if (isEnabled) ColorTextBlack else ColorTextDisabled
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
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.then(
            Modifier
                .clip(RoundedCornerShape(radius))
                .background(ColorBgDisabled)
                .clickableWithoutRipple { onClick() })
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = Body1_B.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorTextSubtle
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

@Composable
fun TopBar(
    start: (@Composable () -> Unit)? = null,
    centerText: String? = null,
    end: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        start?.let {
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                start()
            }
        }

        centerText?.let {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = centerText,
                style = Head2_B.copy(lineHeightStyle = LineHeightStyle.Default),
                color = ColorTextDefault,
            )
        }

        end?.let {
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                end()
            }
        }
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    TopBar()
}

@Composable
fun DogetherTextField(
    modifier: Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    onDone: (() -> Unit)? = null,
    textStyle: TextStyle = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
    textColor: Color = ColorTextDefault,
    hintText: String,
    hintTextStyle: TextStyle = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
    hintTextColor: Color = ColorTextSecondary,
    singleLine: Boolean = true,
    lengthLimit: Int = 0,
) {
    val focusManager = LocalFocusManager.current
    var borderColorState by remember { mutableStateOf(Color.Transparent) }

    Row(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .onFocusChanged { focusState ->
                borderColorState = if (focusState.hasFocus) ColorBorderPrimary else Color.Transparent
            }
            .background(ColorBgElevated)
            .border(
                width = (1.5).dp,
                shape = RoundedCornerShape(12.dp),
                color = borderColorState
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            modifier = Modifier.weight(1f),
            value = value,
            cursorBrush = SolidColor(Color.White),
            onValueChange = { text -> if (lengthLimit != 0 && text.length <= lengthLimit) onValueChanged(text) },
            singleLine = singleLine,
            textStyle = textStyle.copy(color = textColor),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onDone?.invoke()
                }
            ),
            decorationBox = {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    if (value.isEmpty()) {
                        Text(
                            text = hintText,
                            style = hintTextStyle,
                            color = hintTextColor
                        )
                    } else {
                        it()
                    }
                }
            }
        )

        if (lengthLimit != 0) {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = ColorTextPrimary)) {
                        append("${value.length}")
                    }

                    withStyle(SpanStyle(color = ColorTextSecondary)) {
                        append("/${lengthLimit}")
                    }
                },
                style = Small_S.copy(lineHeightStyle = LineHeightStyle.Default)
            )
        }
    }
}