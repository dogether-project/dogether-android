package site.dogether.presentation.screen.my_page.screen.certification_list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import site.dogether.domain.model.todo.MyActivity
import site.dogether.domain.model.todo.Todo
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.my_page.screen.certification_list.model.Chip
import site.dogether.presentation.screen.my_page.screen.certification_list.model.SortingMethod

data class CertificationListUiState(
    val isLoading: Boolean = false,
    val myActivity: MyActivity = MyActivity(),
    val selectedSortingMethod: SortingMethod = SortingMethod.DescendTodoCompleted,
    val chips: ImmutableList<Chip> = persistentListOf(Chip.ReviewPending, Chip.Approve, Chip.Reject),
    val selectedChip: Chip? = null,
    val isSelectSortingMethodBottomSheetShowing: Boolean = false,
    val isDetailMode: Boolean = false,
    val detailedCertifications: ImmutableList<Todo> = persistentListOf(),
    val selectedItemIndex: Int = 0,
    val detailTitle: String = "",
)

sealed interface CertificationListUiEvent : UiEvent {
    sealed interface Click : CertificationListUiEvent {
        data object OnClickSelectSortingMethod : Click

        data class OnClickSortingMethod(val sortingMethod: SortingMethod) : Click

        data class OnClickChip(val chip: Chip?) : Click

        data class OnClickCertificationInfo(
            val detailedCertifications: ImmutableList<Todo>,
            val index: Int
        ) : Click

        data object OnClickBackButtonWhenDetailMode : Click
    }

    sealed interface Callback : CertificationListUiEvent {
        data object OnSelectSortingMethodBottomSheetDismissRequested : Callback

        data object OnScrollReachedBottom : Callback

        data object OnSwipeLeft : Callback

        data object OnSwipeRight : Callback
    }
}

sealed interface CertificationListUiEffect : UiEffect {

}