package site.dogether.presentation.screen.home.state

import androidx.annotation.StringRes
import site.dogether.presentation.R

enum class Chip(@field:StringRes val stringId: Int) {
    All(R.string.common_all),
    Approve(R.string.common_approve),
    Reject(R.string.common_reject),
    ReviewPending(R.string.common_review_pending),
}