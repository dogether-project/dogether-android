package site.dogether.presentation.screen.on_boarding.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import site.dogether.presentation.R

enum class OnBoardingPage(
    @field:StringRes val titleStringId: Int,
    @field:StringRes val bodyStringId: Int,
    @field:DrawableRes val imageId: Int,
) {
    Page0(
        titleStringId = R.string.title_on_boarding_0,
        bodyStringId = R.string.body_on_boarding_0,
        imageId = R.drawable.img_on_boarding_0
    ),
    Page1(
        titleStringId = R.string.title_on_boarding_1,
        bodyStringId = R.string.body_on_boarding_1,
        imageId = R.drawable.img_on_boarding_1
    ),
    Page2(
        titleStringId = R.string.title_on_boarding_2,
        bodyStringId = R.string.body_on_boarding_2,
        imageId = R.drawable.img_on_boarding_2
    )
}