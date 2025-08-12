package site.dogether.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import site.dogether.domain.model.group.Group
import site.dogether.presentation.R
import site.dogether.presentation.theme.Body1_B
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.ColorBgDim
import site.dogether.presentation.theme.ColorBgDisabled
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBgPrimary
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorBorderDisabled
import site.dogether.presentation.theme.ColorBorderPrimary
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorIconElevated
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorTextBlack
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextDisabled
import site.dogether.presentation.theme.ColorTextPrimary
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Grey0
import site.dogether.presentation.theme.Grey900
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.theme.Head2_B
import site.dogether.presentation.theme.Small_S
import site.dogether.presentation.utils.DATE_FORMAT_SHORT_YEAR
import site.dogether.presentation.utils.clickableWithoutRipple
import site.dogether.presentation.utils.conditionedClickableWithoutRipple
import site.dogether.presentation.utils.toFormattedString
import site.dogether.presentation.utils.today
import site.dogether.presentation.utils.tomorrow

@Composable
fun CTAButton(
    modifier: Modifier,
    isEnabled: Boolean = true,
    radius: Dp = 12.dp,
    text: String,
    color: Color = ColorBgPrimary,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.then(
            Modifier
                .clip(RoundedCornerShape(radius))
                .background(if (isEnabled) color else ColorBgDisabled)
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
    modifier: Modifier = Modifier,
    start: (@Composable () -> Unit)? = null,
    centerText: String? = null,
    end: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
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

    var inner by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }

    LaunchedEffect(value) {
        if (inner.composition == null && value != inner.text) {
            inner = inner.copy(text = value, selection = TextRange(value.length))
        }
    }

    Row(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .onFocusChanged { focusState -> borderColorState = if (focusState.hasFocus) ColorBorderPrimary else Color.Transparent }
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
            value = inner,
            cursorBrush = SolidColor(Color.White),
            onValueChange = { newValue ->
                when (lengthLimit) {
                    0 -> {
                        onValueChanged(newValue.text)
                    }

                    else -> {
                        val limitedText = newValue.text.take(lengthLimit)
                        val fixed = newValue.copy(text = limitedText)
                        inner = fixed
                        if (limitedText != value) onValueChanged(limitedText)
                    }
                }
            },
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
                Box(Modifier.padding(horizontal = 16.dp)) {
                    if (inner.text.isEmpty()) {
                        Text(text = hintText, style = hintTextStyle, color = hintTextColor)
                    } else {
                        it()
                    }
                }
            }
        )

        if (lengthLimit > 0) {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = ColorTextPrimary)) { append("${inner.text.length}") }
                    withStyle(SpanStyle(color = ColorTextSecondary)) { append("/$lengthLimit") }
                },
                style = Small_S.copy(lineHeightStyle = LineHeightStyle.Default)
            )
        }
    }
}

@Preview
@Composable
private fun DogetherTextFieldPreview() {
    DogetherTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        value = "",
        onValueChanged = {},
        hintText = "힌트"
    )
}

@Composable
fun BackButton(onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .clickableWithoutRipple { onClick() },
        painter = painterResource(R.drawable.ic_arrow_back),
        tint = ColorIconDefault,
        contentDescription = "icon_arrow_back"
    )
}

@Preview
@Composable
private fun BackButtonPreview() {
    BackButton {}
}

@Composable
fun GroupInfoBoard(
    modifier: Modifier,
    name: String,
    duration: Int,
    maximumMemberCount: Int,
    isLaunchFromToday: Boolean,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(ColorBgSurface)
            .border(
                width = 1.dp, shape = RoundedCornerShape(12.dp), color = ColorBorderDisabled
            )
            .padding(
                horizontal = 20.dp, vertical = 24.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            style = Head1_B.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorTextDefault
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 24.dp),
            thickness = 1.dp,
            color = ColorBorderDisabled
        )

        Column(
            modifier = Modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoRow(
                title = stringResource(R.string.info_duration),
                value = if (duration < 7) {
                    "$duration" + stringResource(R.string.unit_day)
                } else {
                    "${duration / 7}" + stringResource(R.string.unit_week)
                },
            )

            InfoRow(
                title = stringResource(R.string.info_group_member_count),
                value = stringResource(R.string.unit_prefix_whole) + " $maximumMemberCount" + stringResource(R.string.unit_member)
            )

            InfoRow(
                title = stringResource(R.string.info_launch_date),
                value = if (isLaunchFromToday) today.toFormattedString(DATE_FORMAT_SHORT_YEAR) else tomorrow.toFormattedString(
                    DATE_FORMAT_SHORT_YEAR
                )
            )

            InfoRow(
                title = stringResource(R.string.info_end_date),
                value = if (isLaunchFromToday) today.plusDays(duration.toLong()).toFormattedString(DATE_FORMAT_SHORT_YEAR) else tomorrow.plusDays(duration.toLong()).toFormattedString(DATE_FORMAT_SHORT_YEAR)
            )
        }
    }
}

@Preview
@Composable
private fun GroupInfoBoardPreview() {
    GroupInfoBoard(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        name = "Name",
        duration = 21,
        maximumMemberCount = 10,
        isLaunchFromToday = true
    )
}

@Composable
private fun InfoRow(
    title: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = Body1_B.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorTextPrimary
        )

        Text(
            text = value,
            style = Body1_R.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorTextSubtle
        )
    }
}

@Preview
@Composable
private fun InfoRowPreview() {
    InfoRow(
        title = "Title",
        value = "Value"
    )
}

@Composable
fun GroupInfoColumn(
    title: String,
    value: String,
) {
    Column {
        Text(
            text = title,
            style = Body2_R.copy(lineHeightStyle = LineHeightStyle.Default.copy(trim = LineHeightStyle.Trim.None)),
            color = ColorTextSecondary
        )

        Text(
            text = value,
            style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default.copy(trim = LineHeightStyle.Trim.None)),
            color = ColorTextDefault
        )
    }
}

@Preview
@Composable
private fun GroupInfoColumnPreview() {
    GroupInfoColumn(
        title = "Title",
        value = "value"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGroupBottomSheet(
    sheetState: SheetState,
    selectedGroup: Group,
    groups: List<Group>,
    isAddButtonShowing: Boolean,
    onDismissRequest: () -> Unit,
    onClickGroupItem: (Group) -> Unit,
    onClickAddGroup: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        dragHandle = null,
        containerColor = ColorBgSurface,
        scrimColor = ColorBgDim,
        onDismissRequest = { onDismissRequest() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorBgSurface)
                .padding(
                    top = 24.dp,
                    start = 24.dp,
                    end = 24.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.title_select_group),
                    style = Head2_B,
                    color = ColorTextDefault
                )

                Text(
                    text = stringResource(R.string.cta_button_confirm),
                    style = Body1_S,
                    color = ColorTextDefault
                )
            }

            groups.forEach {
                GroupItem(
                    group = it,
                    isSelected = it == selectedGroup,
                    onClick = { onClickGroupItem(it) }
                )
            }

            if (isAddButtonShowing) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickableWithoutRipple { onClickAddGroup() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add_todo),
                        tint = ColorIconElevated,
                        contentDescription = "icon_add_group"
                    )

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(R.string.cta_button_add_group),
                        style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                        color = ColorTextSubtle
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupItem(
    group: Group,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickableWithoutRipple { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = group.name,
            style = Body1_B.copy(lineHeightStyle = LineHeightStyle.Default),
            color = if (isSelected) ColorTextPrimary else ColorTextDisabled
        )

        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                tint = ColorIconPrimary,
                contentDescription = "icon_check"
            )
        }
    }
}

@Preview
@Composable
private fun GroupItemPreview() {
    GroupItem(
        group = Group(),
        isSelected = true,
        onClick = {}
    )
}

/**
 * 두게더 공용 스낵바(토스트)
 * snackbarHost 지정 필요
 * @see site.dogether.presentation.screen.todo.CreateTodoScreen
 * */
@Composable
fun DogetherSnackbar(
    message: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .background(
                color = Grey900,
                shape = RoundedCornerShape(12.dp)
            )
            .clickableWithoutRipple { onDismiss() }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Check icon
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = ColorIconPrimary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = "success",
                    tint = ColorTextDefault,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Message text
            Text(
                text = message,
                style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                color = Grey0,
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
private fun DogetherSnackbarPreview() {
    DogetherSnackbar(
        message = "텍스트",
        onDismiss = {}
    )
}