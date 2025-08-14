package site.dogether.presentation.screen.create_group.model

import androidx.annotation.StringRes
import site.dogether.presentation.R

enum class CreateGroupPage(@field:StringRes val titleStringId: Int) {
    Purpose(R.string.title_create_group_purpose),
    Schedule(R.string.title_create_group_schedule),
    Check(R.string.title_create_group_check)
}