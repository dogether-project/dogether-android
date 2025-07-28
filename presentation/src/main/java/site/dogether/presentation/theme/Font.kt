package site.dogether.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import site.dogether.presentation.R
import site.dogether.presentation.R.font.pretendard_extra_light
import site.dogether.presentation.utils.toSp

val Pretendard = FontFamily(
    Font(R.font.pretendard_thin, FontWeight.ExtraLight),
    Font(pretendard_extra_light, FontWeight.ExtraLight),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extra_bold, FontWeight.ExtraBold),
    Font(pretendard_extra_light, FontWeight.ExtraLight),
)

val Emphasis1_B: TextStyle
    @Composable get() {
        val fontSize = 36.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            lineHeight = fontSize,
            letterSpacing = -(fontSize * 0.02f)
        )
    }

val Emphasis2_B: TextStyle
    @Composable get() {
        val fontSize = 28.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            lineHeight = 45.dp.toSp(),
            letterSpacing = -(fontSize * 0.02f)
        )
    }

val Head1_B: TextStyle
    @Composable get() {
        val fontSize = 24.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            lineHeight = 36.dp.toSp(),
            letterSpacing = -(fontSize * 0.02f)
        )
    }

val Head2_B: TextStyle
    @Composable get() {
        val fontSize = 18.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            lineHeight = 28.dp.toSp(),
            letterSpacing = -(fontSize * 0.02f)
        )
    }

val Body1_B: TextStyle
    @Composable get() {
        val fontSize = 16.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            lineHeight = 25.dp.toSp(),
            letterSpacing = -(fontSize * 0.02f)
        )
    }

val Body1_S: TextStyle
    @Composable get() {
        val fontSize = 16.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize,
            lineHeight = 25.dp.toSp(),
            letterSpacing = -(fontSize * 0.02f)
        )
    }

val Body2_S: TextStyle
    @Composable get() {
        val fontSize = 14.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize,
            lineHeight = 21.dp.toSp(),
            letterSpacing = -(fontSize * 0.02f)
        )
    }

val Small_S: TextStyle
    @Composable get() {
        val fontSize = 12.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize,
            lineHeight = 18.dp.toSp(),
            letterSpacing = -(fontSize * 0.02f)
        )
    }

val Head1_R: TextStyle
    @Composable get() {
        val fontSize = 24.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
            lineHeight = 36.dp.toSp(),
            letterSpacing = -(fontSize * 0.02f)
        )
    }

val Body1_R: TextStyle
    @Composable get() {
        val fontSize = 16.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
            lineHeight = 25.dp.toSp(),
            letterSpacing = -(fontSize * 0.02f)
        )
    }

val Body2_R: TextStyle
    @Composable get() {
        val fontSize = 14.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
            lineHeight = 21.dp.toSp(),
            letterSpacing = -(fontSize * 0.02f)
        )
    }

val Small_R: TextStyle
    @Composable get() {
        val fontSize = 12.dp.toSp()

        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
            lineHeight = 18.dp.toSp(),
            letterSpacing = -(fontSize * 0.02f)
        )
    }