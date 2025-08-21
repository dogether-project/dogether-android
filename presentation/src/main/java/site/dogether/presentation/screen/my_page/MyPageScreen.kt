package site.dogether.presentation.screen.my_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import site.dogether.presentation.R
import site.dogether.presentation.composables.CTAButton
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.screen.my_page.model.Menu
import site.dogether.presentation.theme.Body1_R
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.ColorBorderDisabled
import site.dogether.presentation.theme.ColorIconDefault
import site.dogether.presentation.theme.ColorIconElevated
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextSubtle
import site.dogether.presentation.theme.Head2_B
import site.dogether.presentation.utils.ScreenPreview
import site.dogether.presentation.utils.clickableWithoutRipple

private val MENU_LIST: List<Menu> = Menu.entries

@Composable
fun MyPageScreen(viewModel: MyPageViewModel = koinViewModel()) {
    MyPageScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

@Composable
private fun MyPageScreenContents(
    uiState: MyPageUiState,
    onEvent: (MyPageUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        TopBar(
            start = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    tint = ColorIconDefault,
                    contentDescription = "icon_arrow_back"
                )
            },
            centerText = stringResource(R.string.title_my_page)
        )

        uiState.userInfo?.let { userInfo ->
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_profile),
                    tint = Color.Unspecified,
                    contentDescription = "icon_profile"
                )

                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = userInfo.name,
                    style = Head2_B.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextDefault
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .border(
                        width = (1.5).dp,
                        color = ColorBorderDisabled,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.img_dosik_my_page),
                    contentDescription = "image_dosik_my_page"
                )

                Text(
                    text = stringResource(R.string.body_my_page),
                    style = Body1_S,
                    color = ColorTextDefault
                )

                CTAButton(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .fillMaxWidth()
                        .height(50.dp),
                    radius = 8.dp,
                    text = stringResource(R.string.cta_button_navigate_to_statistics),
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            MENU_LIST.forEach { menu ->
                MenuItem(
                    menu = menu,
                    tint = when (menu) {
                        Menu.CertificationList -> ColorIconPrimary
                        else -> ColorIconElevated
                    },
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun MenuItem(
    menu: Menu,
    tint: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickableWithoutRipple { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(menu.iconId),
            tint = tint,
            contentDescription = "icon_menu_item"
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = stringResource(menu.stringId),
            style = Body1_R.copy(lineHeightStyle = LineHeightStyle.Default),
            color = ColorTextSubtle
        )
    }
}

@ScreenPreview
@Composable
private fun MyPageScreenContentsPreview() {
    MyPageScreenContents(
        uiState = MyPageUiState(),
        onEvent = {}
    )
}