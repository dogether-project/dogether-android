package site.dogether.presentation.screen.my_page.screen.certification_list

import site.dogether.domain.model.todo.MyActivity
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.my_page.screen.certification_list.model.Chip
import site.dogether.presentation.screen.my_page.screen.certification_list.model.SortingMethod

data class CertificationListUiState(
    val isLoading: Boolean = false,
    val myActivity: MyActivity = MyActivity(),
    val selectedSortingMethod: SortingMethod = SortingMethod.DescendTodoCompleted,
    val chips: List<Chip> = listOf(Chip.ReviewPending, Chip.Approve, Chip.Reject),
    val selectedChip: Chip? = null,
    val isSelectSortingMethodBottomSheetShowing: Boolean = false,
)

sealed interface CertificationListUiEvent : UiEvent {
    sealed interface Click : CertificationListUiEvent {
        data object OnClickSelectSortingMethod : Click

        data class OnClickSortingMethod(val sortingMethod: SortingMethod) : Click

        data class OnClickChip(val chip: Chip?) : Click
    }

    sealed interface Callback : CertificationListUiEvent {
        data object OnSelectSortingMethodBottomSheetDismissRequested : Callback
    }
}

sealed interface CertificationListUiEffect : UiEffect {

}