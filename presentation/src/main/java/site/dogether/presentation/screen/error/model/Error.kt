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
    ),
    ParticipateGroup(
        titleStringId = R.string.title_error_participate_group,
        positiveButtonStringId = R.string.cta_button_go_back
    ),
    AlreadyParticipated(
        titleStringId = R.string.title_error_already_participated,
        bodyStringId = R.string.body_error_already_participated,
        negativeButtonStringId = R.string.cta_button_go_back,
        positiveButtonStringId = R.string.cta_button_create_new_group
    ),
    FullHouse(
        titleStringId = R.string.title_error_full_house,
        bodyStringId = R.string.body_error_full_house,
        negativeButtonStringId = R.string.cta_button_go_back,
        positiveButtonStringId = R.string.cta_button_create_new_group
    ),
    InvalidGroup(
        titleStringId = R.string.title_error_invalid_group,
        bodyStringId = R.string.body_error_invalid_group,
        negativeButtonStringId = R.string.cta_button_go_back,
        positiveButtonStringId = R.string.cta_button_create_new_group
    )
}