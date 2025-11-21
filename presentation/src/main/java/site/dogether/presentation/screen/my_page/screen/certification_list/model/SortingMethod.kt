package site.dogether.presentation.screen.my_page.screen.certification_list.model

import androidx.annotation.StringRes
import site.dogether.presentation.R

enum class SortingMethod(
    @field:StringRes val stringId: Int,
    val serverString: String,
) {
    DescendGroupCreated(
        stringId = R.string.common_ascend_group_created,
        serverString = "GROUP_CREATED_AT"
    ),
    DescendTodoCompleted(
        stringId = R.string.common_ascend_todo_completed,
        serverString = "TODO_COMPLETED_AT"
    )
}