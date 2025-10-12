package site.dogether.presentation.screen.my_cert_info

import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.my_cert_info.model.Chip

data class MyCertInfoUiState(
    val isLoading: Boolean = false,
    val items: List<String> = listOf(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
    ),
    val selectedItemIndex: Int = 0,
    val chip: Chip = Chip.Approve,
    val title: String = "Test Title",
)

sealed interface MyCertInfoUiEvent : UiEvent {
    sealed interface Click : MyCertInfoUiEvent {
        data class OnClickItem(val index: Int) : Click
    }
}

sealed interface MyCertInfoUiEffect : UiEffect {

}