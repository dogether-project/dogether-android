package site.dogether.presentation.screen.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import site.dogether.domain.model.user.RankingMember
import site.dogether.presentation.R
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.composables.TopBar
import site.dogether.presentation.theme.Body1_B
import site.dogether.presentation.theme.Body1_S
import site.dogether.presentation.theme.Body2_S
import site.dogether.presentation.theme.BrushProfileBorder
import site.dogether.presentation.theme.ColorBgSurface
import site.dogether.presentation.theme.ColorBorderDisabled
import site.dogether.presentation.theme.ColorIconPrimary
import site.dogether.presentation.theme.ColorIconSecondary
import site.dogether.presentation.theme.ColorTextDefault
import site.dogether.presentation.theme.ColorTextDisabled
import site.dogether.presentation.theme.ColorTextPrimary
import site.dogether.presentation.utils.CollectEffect
import site.dogether.presentation.utils.ScreenPreview

@Composable
fun RankingScreen(viewModel: RankingViewModel = koinViewModel()) {

    viewModel.CollectEffect<RankingUiEffect> { uiEffect ->
        when (uiEffect) {
            else -> Unit
        }
    }

    RankingScreenContents(
        uiState = viewModel.collectAsState().value,
        onEvent = { uiEvent -> viewModel.onEvent(uiEvent) }
    )
}

@Composable
private fun RankingScreenContents(
    uiState: RankingUiState,
    onEvent: (UiEvent) -> Unit
) {
    if (!uiState.isLoading) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            TopBar(
                start = {

                }, centerText = stringResource(R.string.title_ranking)
            )

            Row(
                modifier = Modifier
                    .padding(top = 26.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                val inRankMembers = uiState.inRankMembers

                RankingMemberCard(inRankMembers[1])
                RankingMemberCard(inRankMembers[0])
                RankingMemberCard(inRankMembers[2])
            }

            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(
                        width = 1.dp,
                        color = ColorBorderDisabled,
                        shape = RoundedCornerShape(8.dp)
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.ic_caution),
                    tint = ColorIconSecondary,
                    contentDescription = "ic_caution"
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(R.string.body_ranking),
                    style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
                    color = ColorTextDisabled
                )
            }

            if (uiState.outRankMembers.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(uiState.outRankMembers) { member ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = member.rank.toString(),
                                    style = Body2_S.copy(lineHeightStyle = LineHeightStyle.Default),
                                    color = ColorTextDefault,
                                )

                                Box(
                                    modifier = Modifier
                                        .padding(start = 20.dp)
                                        .size(50.dp)
                                        .border(
                                            width = 1.dp,
                                            brush = BrushProfileBorder,
                                            shape = CircleShape
                                        )
                                )

                                Text(
                                    modifier = Modifier.padding(start = 12.dp),
                                    text = member.name,
                                    style = Body1_S,
                                    color = ColorTextDefault
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(R.drawable.ic_achieved),
                                    tint = ColorIconPrimary,
                                    contentDescription = "ic_achieved"
                                )

                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp),
                                    text = "${member.achievementRate}%",
                                    style = Body1_S.copy(lineHeightStyle = LineHeightStyle.Default),
                                    color = ColorTextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.RankingMemberCard(rankingMember: RankingMember?) {
    Box(modifier = Modifier.weight(1f)) {
        Column(
            modifier = Modifier
                .padding(top = if (rankingMember?.rank == 1) 0.dp else 20.dp)
                .fillMaxWidth()
                .background(
                    color = ColorBgSurface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            rankingMember?.let {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .border(
                            width = 2.dp,
                            brush = BrushProfileBorder,
                            shape = CircleShape
                        )
                ) {

                }
            } ?: run {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(52.dp)
                        .border(
                            width = 2.dp,
                            color = Color.White.copy(0.1f),
                            shape = CircleShape
                        )
                )
            }

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = rankingMember?.name ?: "-",
                style = Body1_B,
                color = ColorTextDefault,
            )

            Text(
                text = rankingMember?.achievementRate?.let { stringResource(R.string.unit_achieve_ratio, it) } ?: "-",
                style = Body2_S,
                color = ColorTextPrimary,
            )
        }

        rankingMember?.let {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = if (rankingMember.rank == 1) (-16).dp else 0.dp),
                painter = painterResource(
                    when (rankingMember.rank) {
                        1 -> R.drawable.ic_1st
                        2 -> R.drawable.ic_2nd
                        3 -> R.drawable.ic_3rd
                        else -> throw IllegalStateException()
                    }
                ),
                tint = Color.Unspecified,
                contentDescription = "ic_ranking"
            )
        }
    }
}

@ScreenPreview
@Composable
private fun RankingScreenContentsPreview() {
    RankingScreenContents(
        uiState = RankingUiState(), onEvent = {})
}