package site.dogether.presentation.screen.todo.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.domain.model.todo.Todo
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.ActionDialog
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.DogetherTextField
import site.dogether.presentation.composables.node.throttledClickable
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_R
import site.dogether.presentation.theme.ColorBorderDefault
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorIconSecondary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextDisabled
import site.dogether.presentation.theme.ColorTextPrimary
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.theme.Head2_B
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.LifecycleEvent

@Composable
fun CreateTodoScreen(
    viewModel: CreateTodoViewModel = koinViewModel()
) {
    val uiState = viewModel.collectAsState().value

    LifecycleEvent(Lifecycle.Event.ON_START) {
        viewModel.onEvent(CreateTodoUiEvent.Lifecycle.OnStart)
    }

    viewModel.CollectEffect<CreateTodoUiEffect> { sideEffect ->
        when (sideEffect) {
            is CreateTodoUiEffect.Back -> Unit
        }
    }

    CreateTodoScreenContents(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )

    InitDialog(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun CreateTodoScreenContents(
    uiState: CreateTodoUiState = CreateTodoUiState(),
    onEvent: (UiEvent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onClick = { onEvent(UiEvent.Click.OnClickBack) })

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.create_todo_screen_title),
                    style = Head2_B.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextDefault,
                )
            }

            Spacer(modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = uiState.formattedDate,
            style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorTextSecondary
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.create_todo_add_count_prefix))
                withStyle(SpanStyle(color = ColorTextPrimary)) {
                    append(" ${uiState.todoItems.filter { it.content.isNotBlank() }.size}")
                }
                withStyle(SpanStyle(color = ColorTextSecondary)) {
                    append("/${uiState.maxTodoCount}")
                }
            },
            style = Head1_B,
            color = ColorTextDefault
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DogetherTextField(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                value = uiState.todoText,
                onValueChanged = { text -> onEvent(CreateTodoUiEvent.UpdateTodoText(text)) },
                hintText = stringResource(R.string.create_todo_input_hint),
                lengthLimit = 20,
            )

            Spacer(Modifier.width(10.dp))

            AddTodoButton(
                isEnabled = uiState.todoText.isNotBlank(),
                onClick = { onEvent(CreateTodoUiEvent.AddTodoItem) }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Content with overlay CTA
        Box(modifier = Modifier.weight(1f)) {
            // Background content
            if (uiState.todoItems.isEmpty()) {
                EmptyListArea(
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    itemsIndexed(uiState.todoItems) { index, todoItem ->
                        TodoListItem(
                            text = todoItem.content,
                            isEditable = todoItem.id == 0L,
                            onDelete = {
                                onEvent(CreateTodoUiEvent.RemoveTodoItem(index))
                            }
                        )
                    }
                }
            }

            // Overlay CTA Button at bottom
            Column(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                CTAButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    isEnabled = uiState.ctaEnabled,
                    text = stringResource(R.string.create_todo_save_button),
                    onClick = { onEvent(CreateTodoUiEvent.CreateTodos) }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun InitDialog(
    uiState: CreateTodoUiState,
    onEvent: (UiEvent) -> Unit
) {
    CheckDialog(
        uiState = uiState,
        onEvent = onEvent
    )
}

@Composable
private fun TodoListItem(
    text: String,
    isEditable: Boolean = false,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ColorBorderDefault.copy(alpha = 0.2f),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = Head2_B.copy(
                    lineHeightStyle = LineHeightStyle.Default.copy(
                        alignment = LineHeightStyle.Alignment.Center
                    )
                ),
                maxLines = 1,
                color = ColorTextDefault.takeIf { isEditable } ?: ColorTextDisabled,
            )

            if (isEditable) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .throttledClickable { onDelete() },
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "delete_todo",
                    tint = ColorTextSecondary
                )
            }
        }
    }
}

@Composable
private fun AddTodoButton(
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(
                color = ColorIconPrimary.takeIf { isEnabled }
                    ?: ColorBorderDefault.copy(alpha = 0.2f),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
            )
            .throttledClickable {
                if (isEnabled) {
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = "add_todo",
            tint = ColorTextDefault.takeIf { isEnabled } ?: ColorIconSecondary
        )
    }
}

@Composable
private fun EmptyListArea(
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .size(width = 200.dp, height = 200.dp),
            painter = painterResource(R.drawable.img_no_todo),
            alignment = Alignment.Center,
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.create_todo_empty_message),
            style = Head2_B,
            color = ColorTextSubtle
        )

        Text(
            text = stringResource(R.string.body_certification_list_not_exist),
            style = Body2_R,
            color = ColorTextDisabled
        )
    }
}

@Composable
private fun CheckDialog(
    uiState: CreateTodoUiState,
    onEvent: (UiEvent) -> Unit,
) {
    if (uiState.createTodoCheckDialogState.isShowing) {
        ActionDialog(
            title = stringResource(R.string.create_todo_dialog_title),
            body = stringResource(R.string.create_todo_dialog_body),
            icon = painterResource(R.drawable.ic_notice),
            negativeText = stringResource(R.string.dialog_button_back),
            positiveText = stringResource(R.string.create_todo_dialog_save),
            onClickPositive = { onEvent(CreateTodoUiEvent.CheckDialog.Confirm) },
            onClickNegative = { onEvent(CreateTodoUiEvent.CheckDialog.Cancel) },
            onDismissRequest = { onEvent(CreateTodoUiEvent.CheckDialog.Cancel) }
        )
    }
}

@Preview(
    name = "빈 상태",
    showBackground = true,
    backgroundColor = 0xFF101010
)
@Composable
private fun CreateTodoEmptyPreview() {
    CreateTodoScreenContents(
        uiState = CreateTodoUiState(
            addEnabled = false
        )
    )
}

@Preview(
    name = "아이템 차 있는 경우",
    showBackground = true,
    backgroundColor = 0xFF101010
)

@Composable
private fun CreateTodoContentsPreview() {
    CreateTodoScreenContents(
        uiState = CreateTodoUiState(
            todoItems = persistentListOf(Todo(content = "투두리스트")),
            addEnabled = true
        )
    )
}