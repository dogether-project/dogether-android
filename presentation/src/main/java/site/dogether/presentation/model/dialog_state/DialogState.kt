package site.dogether.presentation.model.dialog_state

data class DialogState(
    override val isShowing: Boolean = false,
) : BaseDialogState(isShowing)