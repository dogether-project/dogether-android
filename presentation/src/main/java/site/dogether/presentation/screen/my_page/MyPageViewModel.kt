package site.dogether.presentation.screen.my_page

import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class MyPageViewModel : BaseViewModel<MyPageUiState>(MyPageUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            else -> Unit
        }
    }
}