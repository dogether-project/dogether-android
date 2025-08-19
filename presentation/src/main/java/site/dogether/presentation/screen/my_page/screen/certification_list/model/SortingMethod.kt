package site.dogether.presentation.screen.my_page.screen.certification_list.model

import androidx.annotation.StringRes
import site.dogether.presentation.R

enum class SortingMethod(@field:StringRes val stringId: Int) {
    AscendGroupCreated(R.string.common_ascend_group_created), AscendTodoCompleted(R.string.common_ascend_todo_completed)
}