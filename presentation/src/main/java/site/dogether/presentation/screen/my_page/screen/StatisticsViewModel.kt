package site.dogether.presentation.screen.my_page.screen

import site.dogether.presentation.base.BaseViewModel

class StatisticsViewModel : BaseViewModel<StatisticsUiState, StatisticsUiEvent, StatisticsUiEffect>(StatisticsUiState()) {

    override fun onEvent(event: StatisticsUiEvent) {
        when (event) {
            is StatisticsUiEvent.Click -> {
                when (event) {
                    is StatisticsUiEvent.Click.OnClickChooseGroup -> {
                        updateState { it.copy(isChooseGroupBottomSheetExpanded = true) }
                    }
                }
            }

            is StatisticsUiEvent.Callback -> {
                when (event) {
                    is StatisticsUiEvent.Callback.OnChooseGroupBottomSheetDismissRequested -> {
                        updateState { it.copy(isChooseGroupBottomSheetExpanded = false) }
                    }
                }
            }
        }
    }
}