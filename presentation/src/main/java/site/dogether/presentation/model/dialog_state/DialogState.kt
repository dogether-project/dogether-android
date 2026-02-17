package site.dogether.presentation.model.dialog_state

import androidx.compose.runtime.Immutable

@Immutable
data class DialogState(
    override val isShowing: Boolean = false,
) : BaseDialogState(isShowing)