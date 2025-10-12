package site.dogether.presentation.screen.my_cert_info.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import site.dogether.presentation.R
import site.dogether.presentation.theme.ColorBgPrimary
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
        iconId = R.drawable.ic_approve,
        color = ColorBgPrimary
    ),
    Reject(
        stringId = R.string.common_reject,
        iconId = R.drawable.ic_reject,
        color = Red400
    ),
    ReviewPending(
        stringId = R.string.common_review_pending,
        iconId = R.drawable.ic_review_pending,
        color = Yellow
    ),
}