package site.dogether.presentation.composables

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import site.dogether.presentation.R
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.ColorBgPrimary
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionDialog(
    title: String,
    body: String,
    icon: Painter? = null,
    negativeText: String = "",
    positiveText: String,
    positiveButtonColor: Color = ColorBgPrimary,
    onClickNegative: () -> Unit = {},
    onClickPositive: () -> Unit,
    onDismissRequest: () -> Unit = onClickPositive
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .background(ColorBgSurface)
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    )
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                icon?.let {
                    Icon(
                        painter = icon,
                        tint = ColorIconPrimary,
                        contentDescription = "icon"
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = title,
                    style = Head1_B,
                    textAlign = TextAlign.Center,
                    color = ColorTextDefault
                )

                if (body.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = body,
                        style = Body1_R,
                        color = ColorTextSubtle,
                        textAlign = TextAlign.Center
                    )
                }

                // negativeText가 비어있으면 positive 버튼만 표시
                if (negativeText.isEmpty()) {
                    CTAButton(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        text = positiveText,
                        isEnabled = true,
                        radius = 8.dp,
                        color = positiveButtonColor,
                        onClick = { onClickPositive() }
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .fillMaxWidth()
                    ) {
                        NegativeCTAButton(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            text = negativeText,
                            radius = 8.dp,
                            onClick = { onClickNegative() }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        CTAButton(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            text = positiveText,
                            isEnabled = true,
                            radius = 8.dp,
                            color = positiveButtonColor,
                            onClick = { onClickPositive() }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ActionDialogPreview() {
    ActionDialog(
        title = "Preview",
        body = "preview",
        icon = painterResource(R.drawable.ic_notice),
        negativeText = "negative",
        positiveText = "positive",
        onClickNegative = {},
        onClickPositive = {},
        onDismissRequest = {}
    )
}

@Composable
fun LoadingDialog() {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        val context = LocalContext.current

        val imageLoader = ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        AsyncImage(
            modifier = Modifier.size(120.dp),
            model = R.drawable.anim_loading,
            contentDescription = "anim_loading",
            imageLoader = imageLoader
        )
    }
}