package site.dogether.presentation.screen.home.state

import androidx.annotation.StringRes
import site.dogether.presentation.R

enum class Chip(@field:StringRes val stringId: Int) {
    All(R.string.chip_all),
    Approve(R.string.chip_approve),
    Reject(R.string.chip_reject),
    ReviewPending(R.string.chip_review_pending),
}