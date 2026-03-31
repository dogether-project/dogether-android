package site.dogether.presentation.screen.ranking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import site.dogether.KEY_GROUP_ID
import site.dogether.common.utils.orZero
import site.dogether.domain.model.user.RankingMember
import site.dogether.domain.use_case.group.GetRankingUseCase
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error

class RankingViewModel(
    savedStateHandle: SavedStateHandle,
    private val getRanking: GetRankingUseCase
) : BaseViewModel<RankingUiState>(RankingUiState()) {

    private val groupId: Int by lazy {
        savedStateHandle.get<Int>(KEY_GROUP_ID).orZero()
    }

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is RankingUiEvent.Click.OnClickMember -> {
                postEffect(UiEffect.NavigateTo("${Screen.MEMBER_CERT_INFO}/${event.groupId}/${event.memberId}/${event.memberName}"))
            }
        }
    }

    init {
        loadRanking()
    }

    private fun loadRanking() {
        updateState {
            it.copy(
                isLoading = true,
                groupId = groupId
            )
        }

        viewModelScope.launch {
            getRanking(groupId).onSuccess { rankingInfo ->
                val originalMembers = rankingInfo.list

                val inRankArray = Array(3) { RankingMember() }

                for (i in 0 until kotlin.math.min(originalMembers.size, 3)) {
                    inRankArray[i] = originalMembers[i]
                }

                val outRankMembers = if (originalMembers.size > 3) {
                    originalMembers.subList(3, originalMembers.size)
                } else {
                    emptyList()
                }

                updateState {
                    it.copy(
                        inRankMembers = inRankArray.toImmutableList(),
                        outRankMembers = outRankMembers.toImmutableList(),
                    )
                }
            }.onFailure {
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { loadRanking() }
                    )
                )
            }
        }.invokeOnCompletion {
            updateState { it.copy(isLoading = false) }
        }
    }
}