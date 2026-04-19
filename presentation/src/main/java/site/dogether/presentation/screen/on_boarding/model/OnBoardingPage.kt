package site.dogether.presentation.screen.on_boarding.model

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import site.dogether.presentation.R

enum class OnBoardingPage(
    @field:StringRes val titleStringId: Int,
    @field:StringRes val bodyStringId: Int,
    @field:RawRes val lottieResId: Int,
) {
    Page0(
        titleStringId = R.string.title_on_boarding_0,
        bodyStringId = R.string.body_on_boarding_0,
        lottieResId = R.raw.onboarding1
    ),
    Page1(
        titleStringId = R.string.title_on_boarding_1,
        bodyStringId = R.string.body_on_boarding_1,
        lottieResId = R.raw.onboarding2
    ),
    Page2(
        titleStringId = R.string.title_on_boarding_2,
        bodyStringId = R.string.body_on_boarding_2,
        lottieResId = R.raw.onboarding3
    )
}