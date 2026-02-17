package site.dogether.presentation.screen.home.state

import androidx.compose.runtime.Immutable

@Immutable
data class TooltipUiState(
    val isShowing: Boolean = false,
    val stringId: Int? = null,
)