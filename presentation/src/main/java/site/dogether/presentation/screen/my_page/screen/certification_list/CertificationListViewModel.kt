package site.dogether.presentation.screen.my_page.screen.certification_list

import site.dogether.presentation.base.BaseViewModel

class CertificationListViewModel : BaseViewModel<CertificationListUiState, CertificationListUiEvent, CertificationListUiEffect>(CertificationListUiState()) {

    override fun onEvent(event: CertificationListUiEvent) {
        when (event) {
            is CertificationListUiEvent.Click -> {
                when (event) {
                    is CertificationListUiEvent.Click.OnClickSelectSortingMethod -> {
                        updateState { it.copy(isSelectSortingMethodBottomSheetExpanded = true) }
                    }

                    is CertificationListUiEvent.Click.OnClickSortingMethod -> {
                        updateState {
                            it.copy(
                                selectedSortingMethod = event.sortingMethod,
                                isSelectSortingMethodBottomSheetExpanded = false
                            )
                        }
                    }

                    is CertificationListUiEvent.Click.OnClickChip -> {
                        updateState { it.copy(selectedChip = event.chip) }
                    }
                }
            }

            is CertificationListUiEvent.Callback -> {
                when (event) {
                    is CertificationListUiEvent.Callback.OnSelectSortingMethodBottomSheetDismissRequested -> {
                        updateState { it.copy(isSelectSortingMethodBottomSheetExpanded = false) }
                    }
                }
            }
        }
    }
}