package site.dogether.presentation.screen.my_page.screen.statistics

import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class StatisticsViewModel : BaseViewModel<StatisticsUiState>(StatisticsUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is StatisticsUiEvent.Click -> {
                when (event) {
                    is StatisticsUiEvent.Click.OnClickSelectGroup -> {
                        updateState { it.copy(isSelectGroupBottomSheetShowing = true) }
                    }
                }
            }

            is StatisticsUiEvent.Callback -> {
                when (event) {
                    is StatisticsUiEvent.Callback.OnSelectGroupBottomSheetDismissRequested -> {
                        updateState { it.copy(isSelectGroupBottomSheetShowing = false) }
                    }
                }
            }
        }
    }
}