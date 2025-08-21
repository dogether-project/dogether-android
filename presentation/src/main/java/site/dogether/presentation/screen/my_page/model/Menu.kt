package site.dogether.presentation.screen.my_page.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import site.dogether.presentation.R

enum class Menu(
    @field:StringRes val stringId: Int,
    @field:DrawableRes val iconId: Int,
) {
    CertificationList(
        stringId = R.string.my_page_menu_certification_list,
        iconId = R.drawable.ic_certification_list
    ),
    GroupManager(
        stringId = R.string.my_page_menu_group_management,
        iconId = R.drawable.ic_group_management
    ),
    Settings(
        stringId = R.string.my_page_menu_settings,
        iconId = R.drawable.ic_settings
    )
}