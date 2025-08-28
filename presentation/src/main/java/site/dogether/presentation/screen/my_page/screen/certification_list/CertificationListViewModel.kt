package site.dogether.presentation.screen.my_page.screen.certification_list

import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class CertificationListViewModel : BaseViewModel<CertificationListUiState>(CertificationListUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is CertificationListUiEvent.Click -> {
                when (event) {
                    is CertificationListUiEvent.Click.OnClickSelectSortingMethod -> {
                        updateState { it.copy(isSelectSortingMethodBottomSheetShowing = true) }
                    }

                    is CertificationListUiEvent.Click.OnClickSortingMethod -> {
                        updateState {
                            it.copy(
                                selectedSortingMethod = event.sortingMethod,
                                isSelectSortingMethodBottomSheetShowing = false
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
                        updateState { it.copy(isSelectSortingMethodBottomSheetShowing = false) }
                    }
                }
            }
        }
    }
}