package site.dogether.presentation.screen.todo.certificate

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.Screen
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.BackButton
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.composables.node.throttledClickable
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.ColorBgElevated
import site.dogether.presentation.theme.ColorBorderDisabled
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorIconElevated
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head1_B
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.LifecycleEvent
import site.dogether.presentation.utils.LocalNavHostController
import java.io.File
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CertificateTodoScreen(
    viewModel: CertificateTodoViewModel = koinViewModel(),
) {
    val uiState = viewModel.collectAsState().value
    val context = LocalContext.current
    val navController = LocalNavHostController.current

    // 권한 상태 관리 (Android 13+에서는 READ_MEDIA_IMAGES 사용)
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val storagePermissionState = rememberPermissionState(
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    // 카메라 촬영을 위한 임시 파일 URI
    val cameraImageUri = remember { createImageUri(context) }

    // 이미지 선택/촬영을 위한 Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.setSelectedImage(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSucceeded: Boolean ->
        if (isSucceeded && cameraImageUri != null) {
            viewModel.setSelectedImage(cameraImageUri)
        }
    }

    LifecycleEvent(Lifecycle.Event.ON_START) {
        viewModel.onEvent(CertificateTodoUiEvent.Lifecycle.OnStart)
    }

    // 권한 승인 후 자동 실행
    LaunchedEffect(cameraPermissionState.status.isGranted, uiState.isRequestingCamera) {
        if (cameraPermissionState.status.isGranted && uiState.isRequestingCamera) {
            cameraImageUri?.let { uri ->
                cameraLauncher.launch(uri)
            }
        }
    }

    LaunchedEffect(storagePermissionState.status.isGranted, uiState.isRequestingGallery) {
        if (storagePermissionState.status.isGranted && uiState.isRequestingGallery) {
            imagePickerLauncher.launch("image/*")
        }
    }

    // 권한 거부 시 요청 상태 초기화
    LaunchedEffect(cameraPermissionState.status.isGranted, uiState.isRequestingCamera) {
        if (!cameraPermissionState.status.isGranted && uiState.isRequestingCamera) {
            viewModel.setRequestingCamera(false)
        }
    }

    LaunchedEffect(storagePermissionState.status.isGranted, uiState.isRequestingGallery) {
        if (!storagePermissionState.status.isGranted && uiState.isRequestingGallery) {
            viewModel.setRequestingGallery(false)
        }
    }

    viewModel.CollectEffect<CertificateTodoUiEffect> { uiEffect ->
        when (uiEffect) {
            is CertificateTodoUiEffect.Back -> Unit

            is CertificateTodoUiEffect.OpenGallery -> {
                if (storagePermissionState.status.isGranted) {
                    imagePickerLauncher.launch("image/*")
                } else {
                    viewModel.setRequestingGallery(true)
                    storagePermissionState.launchPermissionRequest()
                }
            }

            is CertificateTodoUiEffect.OpenCamera -> {
                if (cameraPermissionState.status.isGranted) {
                    cameraImageUri?.let { uri ->
                        cameraLauncher.launch(uri)
                    } ?: run {
                        viewModel.setError(context.getString(R.string.certificate_todo_camera_error))
                    }
                } else {
                    viewModel.setRequestingCamera(true)
                    cameraPermissionState.launchPermissionRequest()
                }
            }

            is CertificateTodoUiEffect.NavigateToNext -> {
                uiState.selectedImageUri?.let { uri ->
                    // URI를 URL 인코딩하여 네비게이션
                    val encodedUri = URLEncoder.encode(uri.toString(), "UTF-8")
                    navController.navigate(
                        route = "${Screen.CERTIFICATE_DESCRIPTION}/${uiState.todoId}/$encodedUri"
                    )
                }
            }
        }
    }

    CertificateTodoScreenContents(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        context = context
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CertificateTodoScreenContents(
    uiState: CertificateTodoUiState = CertificateTodoUiState(),
    onEvent: (UiEvent) -> Unit = {},
    context: Context = LocalContext.current,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        TopBar(
            start = { BackButton { onEvent(UiEvent.Click.OnClickBack) } },
            centerText = context.getString(R.string.certificate_todo_screen_title)
        )

        Spacer(modifier = Modifier.height(40.dp))

        PhotoUploadArea(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            imageUri = uiState.selectedImageUri,
            context = context
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Action Buttons Section
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = uiState.todoTitle,
                style = Head1_B,
                color = ColorTextDefault,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    modifier = Modifier.weight(1f),
                    icon = R.drawable.ic_image,
                    text = context.getString(R.string.certificate_todo_select_photo),
                    onClick = { onEvent(CertificateTodoUiEvent.SelectFromGallery) }
                )

                ActionButton(
                    modifier = Modifier.weight(1f),
                    icon = R.drawable.ic_camera,
                    text = context.getString(R.string.certificate_todo_take_photo),
                    onClick = { onEvent(CertificateTodoUiEvent.TakePhoto) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Next Button
        CTAButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            isEnabled = uiState.selectedImageUri != null,
            text = context.getString(R.string.certificate_todo_next_button),
            onClick = { onEvent(CertificateTodoUiEvent.Next) }
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun PhotoUploadArea(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
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
                color = ColorBorderDisabled,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        imageUri?.let {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxSize(),
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = "certification_image",
                contentScale = ContentScale.Fit
            )
        } ?: run {
            Column(
                modifier = Modifier.padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.weight(1f),
                    painter = painterResource(R.drawable.img_dosik_cam),
                    contentDescription = "certification_placeholder",
                    contentScale = ContentScale.Fit
                )

                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = context.getString(R.string.certificate_todo_upload_placeholder),
                    style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextSubtle,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    icon: Int,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .background(
                color = ColorBgElevated,
                shape = RoundedCornerShape(8.dp)
            )
            .throttledClickable { onClick() }
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(icon),
            contentDescription = text,
            tint = ColorIconDefault
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorIconElevated
        )
    }
}

/**
 * 카메라 촬영을 위한 임시 파일 URI 생성
 */
private fun createImageUri(context: Context): Uri? {
    return try {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = File(context.cacheDir, "images")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    } catch (e: Exception) {
        null
    }
}