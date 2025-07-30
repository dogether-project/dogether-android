package site.dogether.presentation.screen.on_boarding.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnBoardingPageItem(
    @field:StringRes val titleStringId: Int,
    @field:StringRes val bodyStringId: Int,
    @field:DrawableRes val imageId: Int,
)