package site.dogether.presentation.screen.create_group.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

data class CreateGroupPageItem(
    @field:StringRes val titleStringId: Int,
    val content: @Composable () -> Unit
)