package site.dogether.presentation.screen.participate_group

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.KEY_JOIN_CODE
import site.dogether.common.exception.NetworkFailureException
import site.dogether.domain.use_case.group.ParticipateGroupUseCase
import site.dogether.presentation.R
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.dialog_state.ActionDialogState
import site.dogether.presentation.screen.error.model.Error
import site.dogether.presentation.screen.error.model.ErrorCode

class ParticipateGroupViewModel(
    private val participateGroup: ParticipateGroupUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ParticipateGroupUiState>(ParticipateGroupUiState()) {

    init {
        savedStateHandle.get<String>(KEY_JOIN_CODE)?.let { joinCode ->
            updateState { it.copy(joinCode = joinCode) }
        }
    }

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is ParticipateGroupUiEvent.Type -> {
                when (event) {
                    is ParticipateGroupUiEvent.Type.OnJoinCodeTyped -> {
                        updateState { it.copy(joinCode = event.text) }
                    }
                }
            }

            is ParticipateGroupUiEvent.Click -> {
                when (event) {
                    is ParticipateGroupUiEvent.Click.OnClickParticipate -> {
                        participateGroupAction()
                    }

                    is ParticipateGroupUiEvent.Click.OnClickActionDialogNegative -> {
                        dismissActionDialog()
                    }

                    is ParticipateGroupUiEvent.Click.OnClickActionDialogPositive -> {
                        dismissActionDialog()
                    }
                }
            }
        }
    }

    private fun participateGroupAction() {
        updateState { it.copy(isLoading = true) }

        viewModelScope.launch {
            participateGroup(uiState.joinCode).getOrElse {
                val code = (it as? NetworkFailureException)?.code.orEmpty()
                val titleStringId = when(code) {
                    ErrorCode.GROUP_PARTICIPATION_FAIL -> R.string.title_error_participate_group
                    ErrorCode.GROUP_ALREADY_PARTICIPATED -> R.string.title_error_already_participated
                    ErrorCode.GROUP_FULL_HOUSE -> R.string.title_error_full_house
                    ErrorCode.GROUP_INVALID -> R.string.title_error_group_invalid
                    ErrorCode.GROUP_NOT_FOUND -> R.string.title_error_group_invalid
                    else -> R.string.title_error_unknown
                }
                val bodyStringId = when(code) {
                    ErrorCode.GROUP_PARTICIPATION_FAIL -> R.string.body_error_load_data
                    ErrorCode.GROUP_ALREADY_PARTICIPATED -> R.string.body_error_already_participated
                    ErrorCode.GROUP_FULL_HOUSE -> R.string.body_error_full_house
                    ErrorCode.GROUP_INVALID -> R.string.body_error_invalid_group
                    ErrorCode.GROUP_NOT_FOUND -> R.string.body_error_invalid_group
                    else -> R.string.body_error_unknown
                }

                if (titleStringId != R.string.title_error_unknown && bodyStringId != R.string.body_error_unknown) {
                    updateState {
                        it.copy(
                            actionDialogState = ActionDialogState(
                                isShowing = true,
                                titleStringId = titleStringId,
                                bodyStringId = bodyStringId,
                                negativeTextStringId = R.string.cta_button_retype,
                                positiveTextStringId = R.string.cta_button_confirm
                            )
                        )
                    }
                    return@launch
                }

                val error = Error.entries.find { it.errorCode == code } ?: Error.LoadData

                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = error,
                        onPositive = { participateGroupAction() }
                    )
                )
                return@launch
            }

            postEffect(UiEffect.NavigateTo(Screen.HOME, clearBackStack = true))
        }.invokeOnCompletion {
            updateState { it.copy(isLoading = false) }
        }
    }

    private fun dismissActionDialog() {
        updateState {
            it.copy(actionDialogState = it.actionDialogState.copy(isShowing = false))
        }
    }
}