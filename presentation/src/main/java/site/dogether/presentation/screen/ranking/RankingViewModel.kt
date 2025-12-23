package site.dogether.presentation.screen.ranking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.KEY_GROUP_ID
import site.dogether.common.utils.orZero
import site.dogether.domain.model.user.RankingMember
import site.dogether.domain.use_case.group.GetRankingUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

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
            else -> Unit
        }
    }

    init {
        updateState { it.copy(isLoading = true, groupId = groupId) }

        viewModelScope.launch {
            getRanking(groupId).onSuccess { rankingInfo ->
                val originalMembers = rankingInfo.list

                val inRankArray = Array<RankingMember?>(3) { null }

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
                        inRankMembers = inRankArray.toList(),
                        outRankMembers = outRankMembers,
                    )
                }
            }.onFailure {
                // handle error
            }
        }.invokeOnCompletion {
            updateState { it.copy(isLoading = false) }
        }
    }
}