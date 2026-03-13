package site.dogether.presentation.model.dialog_state

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class ActionDialogState(
    override val isShowing: Boolean = false,
    @field:StringRes val titleStringId: Int = 0,
    @field:StringRes val bodyStringId: Int = 0,
    @field:StringRes val negativeTextStringId: Int = 0,
    @field:StringRes val positiveTextStringId: Int = 0
) : BaseDialogState(isShowing)