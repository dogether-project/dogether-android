package site.dogether.presentation.screen.error.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import site.dogether.presentation.R

enum class Error(
    @field:StringRes val titleStringId: Int,
    @field:StringRes val bodyStringId: Int? = null,
    @field:DrawableRes val imageId: Int = R.drawable.img_error_common,
    @field:StringRes val negativeButtonStringId: Int? = null,
    @field:StringRes val positiveButtonStringId: Int,
    val errorCode : String = ""
) {
    Network(
        titleStringId = R.string.title_error_network,
        bodyStringId = R.string.body_error_network,
        imageId = R.drawable.img_error_network,
        positiveButtonStringId = R.string.cta_button_update
    ),
    Unknown(
        titleStringId = R.string.title_error_unknown,
        bodyStringId = R.string.body_error_unknown,
        imageId = R.drawable.img_error_unknown,
        positiveButtonStringId = R.string.cta_button_back_to_home
    ),
    LoadData(
        titleStringId = R.string.title_error_load_data,
        bodyStringId = R.string.body_error_load_data,
        imageId = R.drawable.img_error_load_data,
        positiveButtonStringId = R.string.cta_button_retry
    ),
    SaveTodo(
        titleStringId = R.string.title_error_save_todo,
        positiveButtonStringId = R.string.cta_button_go_back
    ),
    CreateGroup(
        titleStringId = R.string.title_error_create_group,
        positiveButtonStringId = R.string.cta_button_go_back
    )
}

object ErrorCode {
    const val GROUP_PARTICIPATION_FAIL = "CGF-0001"
    const val GROUP_ALREADY_PARTICIPATED = "CGF-0002"
    const val GROUP_FULL_HOUSE = "CGF-0003"
    const val GROUP_INVALID = "CGF-0004"
    const val GROUP_NOT_FOUND = "CGF-0005"
}