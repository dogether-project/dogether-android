package site.dogether.presentation.screen.my_page.screen.statistics

import site.dogether.presentation.base.BaseViewModel

class StatisticsViewModel : BaseViewModel<StatisticsUiState, StatisticsUiEvent, StatisticsUiEffect>(StatisticsUiState()) {

    override fun onEvent(event: StatisticsUiEvent) {
        when (event) {
            is StatisticsUiEvent.Click -> {
                when (event) {
                    is StatisticsUiEvent.Click.OnClickSelectGroup -> {
                        updateState { it.copy(isSelectGroupBottomSheetExpanded = true) }
                    }
                }
            }

            is StatisticsUiEvent.Callback -> {
                when (event) {
                    is StatisticsUiEvent.Callback.OnSelectGroupBottomSheetDismissRequested -> {
                        updateState { it.copy(isSelectGroupBottomSheetExpanded = false) }
                    }
                }
            }
        }
    }
}