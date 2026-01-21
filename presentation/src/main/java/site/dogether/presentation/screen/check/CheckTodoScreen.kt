package site.dogether.presentation.screen.check

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.DogetherTextField
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.theme.Body1_B
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_S
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBgInverse
import site.dogether.presentation.theme.ColorBgPrimary
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorBorderDefault
import site.dogether.presentation.theme.ColorBorderDisabled
import site.dogether.presentation.theme.ColorIconInverse
import site.dogether.presentation.theme.ColorTextBlack
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSecondary
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.LifecycleEvent
import site.dogether.presentation.utils.LocalNavHostController
import site.dogether.presentation.utils.clickableWithoutRipple

@Composable
fun CheckTodoScreen(viewModel: CheckTodoViewModel = koinViewModel()) {
    val uiState = viewModel.collectAsState().value
    val context = LocalContext.current
    val navController = LocalNavHostController.current

    LifecycleEvent(Lifecycle.Event.ON_START) {
        viewModel.onEvent(CheckTodoUiEvent.Lifecycle.OnStart)
    }

    viewModel.CollectEffect<CheckTodoUiEffect> { sideEffect ->
        when (sideEffect) {
            is CheckTodoUiEffect.ReviewSubmitted -> {
                navController.popBackStack()
            }
        }
    }

    CheckTodoScreenContents(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        context = context
    )
}

@Composable
private fun CheckTodoScreenContents(
    uiState: CheckTodoUiState = CheckTodoUiState(),
    onEvent: (UiEvent) -> Unit = {},
    context: Context = LocalContext.current,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            TopBar(
                start = { BackButton { onEvent(UiEvent.Click.OnClickBack) } },
                centerText = "검사하기"
            )

            when {
                uiState.isLoading && uiState.certifications.isEmpty() -> {
                    // 로딩 상태
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ColorBgPrimary)
                    }
                }

                uiState.isEmpty -> {
                    // 검사할 인증이 없는 상태
                    EmptyStateContent()
                }

                else -> {
                    // 검사할 인증이 있는 상태
                    CertificationReviewContent(
                        uiState = uiState,
                        onEvent = onEvent,
                        context = context
                    )
                }
            }
        }

        // 피드백 입력 다이얼로그
        if (uiState.isFeedbackDialogShowing) {
            FeedbackDialog(
                feedback = uiState.reviewFeedback,
                onFeedbackChange = { onEvent(CheckTodoUiEvent.UpdateFeedback(it)) },
                onDismiss = { onEvent(CheckTodoUiEvent.HideFeedbackDialog) },
                onSubmit = {
                    onEvent(CheckTodoUiEvent.UpdateFeedback(uiState.reviewFeedback))
                    onEvent(CheckTodoUiEvent.HideFeedbackDialog)
                }
            )
        }

        // 로딩 오버레이
        if (uiState.isLoading && uiState.certifications.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ColorBgPrimary)
            }
        }
    }
}

@Composable
private fun EmptyStateContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(120.dp),
            painter = painterResource(R.drawable.img_no_todo),
            contentDescription = "empty_state",
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "검사할 인증이 없습니다",
            style = Head1_B,
            color = ColorTextDefault,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "모든 인증을 검사 완료했어요!",
            style = Body1_S,
            color = ColorTextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CertificationReviewContent(
    uiState: CheckTodoUiState,
    onEvent: (CheckTodoUiEvent) -> Unit,
    context: Context,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(20.dp))

        // 안내 문구
        Text(
            text = "투두를 검사해주세요!",
            style = Head1_B,
            color = ColorTextDefault,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.ic_caution),
                contentDescription = "caution",
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "검사 결과는 선택하면 수정할 수 없어요",
                style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
                color = ColorTextSecondary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 인증 이미지 영역
        CertificationImageArea(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp),
            imageUrl = uiState.certificationMediaUrl,
            content = uiState.certificationContent,
            memberName = uiState.memberName,
            context = context
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 투두 제목
        Text(
            text = uiState.todoTitle,
            style = Body1_S,
            color = ColorTextDefault,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 검사 버튼 (노인정 / 인정)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReviewButton(
                modifier = Modifier.weight(1f),
                text = "노인정",
                isSelected = uiState.selectedReviewType == ReviewType.REJECT,
                isApprove = false,
                onClick = { onEvent(CheckTodoUiEvent.SelectReviewType(ReviewType.REJECT)) }
            )

            ReviewButton(
                modifier = Modifier.weight(1f),
                text = "인정",
                isSelected = uiState.selectedReviewType == ReviewType.APPROVE,
                isApprove = true,
                onClick = { onEvent(CheckTodoUiEvent.SelectReviewType(ReviewType.APPROVE)) }
            )
        }

        // 노인정 선택 시 피드백 표시
        if (uiState.selectedReviewType == ReviewType.REJECT && uiState.reviewFeedback.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = ColorBgSurface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = uiState.reviewFeedback,
                    style = Body1_S,
                    color = ColorTextDefault
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 보내기 버튼
        CTAButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            isEnabled = uiState.confirmEnabled,
            text = if (uiState.hasMoreCertifications) "보내기 (${uiState.currentIndex + 1}/${uiState.certifications.size})" else "보내기",
            onClick = { onEvent(CheckTodoUiEvent.SubmitReview) }
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Composable
private fun CertificationImageArea(
    modifier: Modifier = Modifier,
    imageUrl: String,
    content: String,
    memberName: String,
    context: Context,
) {
    Box(
        modifier = modifier
            .background(
                color = ColorBgElevated,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = ColorBorderDefault,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxSize(),
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .build(),
                contentDescription = "certification_image",
                contentScale = ContentScale.Crop
            )

            // 하단 그라데이션 및 텍스트
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 36.dp, vertical = 16.dp)
            ) {
                if (content.isNotEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = content,
                        textAlign = TextAlign.Center,
                        style = Body1_R,
                        color = Color.White
                    )
                }
                if (memberName.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = memberName,
                        textAlign = TextAlign.Center,
                        style = Body1_S,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.size(120.dp),
                    painter = painterResource(R.drawable.img_no_todo),
                    contentDescription = "certification_placeholder",
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "인증 이미지가 없습니다",
                    style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextDefault,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ReviewButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    isApprove: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = when {
        isSelected && isApprove -> ColorBgPrimary
        isSelected && !isApprove -> Color(0xFFDC2626) // Red
        else -> ColorBgInverse
    }

    Box(
        modifier = modifier
            .height(56.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = Color.Transparent.takeIf { isSelected } ?: ColorBorderDefault,
                shape = RoundedCornerShape(12.dp)
            )
            .clickableWithoutRipple { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(
                    R.drawable.ic_approve.takeIf { isApprove } ?: R.drawable.ic_reject
                ),
                tint = ColorIconInverse,
                contentDescription = "icon"
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = text,
                style = Body1_B.copy(lineHeightStyle = LineHeightStyle.Default),
                color = ColorTextBlack
            )
        }
    }
}

@Composable
private fun FeedbackDialog(
    feedback: String,
    onFeedbackChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
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
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.End)
                        .clickableWithoutRipple { onDismiss() },
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "close",
                    tint = ColorTextDefault,
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "이유를 들려주세요!",
                    style = Head1_B,
                    color = ColorTextDefault
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(1.dp, ColorBorderDisabled),
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Image(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.ic_caution),
                            contentDescription = "caution",
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "검사가 완료된 피드백은 바꿀 수 없어요",
                            style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
                            color = ColorTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                DogetherTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    value = feedback,
                    onValueChanged = onFeedbackChange,
                    hintText = "텍스트를 입력하세요",
                    singleLine = false,
                    lengthLimit = 80
                )

                Spacer(modifier = Modifier.height(20.dp))

                CTAButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    isEnabled = feedback.isNotEmpty(),
                    text = "등록하기",
                    onClick = onSubmit
                )
            }
        }
    }
}

