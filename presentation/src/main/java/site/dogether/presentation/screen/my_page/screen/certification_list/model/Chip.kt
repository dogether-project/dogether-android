package site.dogether.presentation.screen.my_page.screen.certification_list.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import site.dogether.presentation.R
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.Red400
import site.dogether.presentation.theme.Yellow

enum class Chip(
    @field:StringRes val stringId: Int,
    @field:DrawableRes val iconId: Int,
    @field:ColorRes val color: Color,
) {
    Approve(
        stringId = R.string.common_approve,
        iconId = R.drawable.ic_approve_summary,
        color = Yellow
    ),
    Reject(
        stringId = R.string.common_reject,
        iconId = R.drawable.ic_reject_summary,
        color = ColorIconPrimary
    ),
    ReviewPending(
        stringId = R.string.common_review_pending,
        iconId = R.drawable.ic_review_pending,
        color = Red400
    )
}