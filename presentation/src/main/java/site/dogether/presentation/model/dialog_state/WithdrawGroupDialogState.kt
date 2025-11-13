package site.dogether.presentation.model.dialog_state

data class WithdrawGroupDialogState(
    override val isShowing: Boolean = false,
    val groupId: Int = -1,
) : BaseDialogState(isShowing)
