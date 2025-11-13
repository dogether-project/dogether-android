package site.dogether.presentation.screen.my_page.screen.group_management

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.group.GetJoiningGroupsUseCase
import site.dogether.domain.use_case.group.WithdrawGroupUseCase
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

class GroupManagementViewModel(
    private val getJoiningGroups: GetJoiningGroupsUseCase,
    private val withdrawGroup: WithdrawGroupUseCase,
) : BaseViewModel<GroupManagementUiState>(GroupManagementUiState()) {

    init {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }

            val getJoiningGroupsResult = getJoiningGroups().getOrElse {
                return@launch
            }

            updateState { it.copy(groups = getJoiningGroupsResult.groups) }
        }.invokeOnCompletion {
            updateState { it.copy(isLoading = false) }
        }
    }

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is GroupManagementUiEvent.Click -> {
                when (event) {
                    is GroupManagementUiEvent.Click.OnClickWithdraw -> {
                        updateState {
                            it.copy(
                                withdrawGroupDialogState = it.withdrawGroupDialogState.copy(
                                    isShowing = true,
                                    groupId = event.groupId
                                )
                            )
                        }
                    }

                    is GroupManagementUiEvent.Click.OnClickWithdrawDialogNegative -> {
                        dismissWithdrawDialog()
                    }

                    is GroupManagementUiEvent.Click.OnClickWithdrawDialogPositive -> {
                        viewModelScope.launch {
                            updateState { it.copy(isLoading = true) }

                            withdrawGroup(uiState.withdrawGroupDialogState.groupId).getOrElse {
                                return@launch
                            }

                            val getJoiningGroupsResult = getJoiningGroups().getOrElse {
                                return@launch
                            }

                            if (getJoiningGroupsResult.groups.isEmpty()) {
                                postEffect(
                                    UiEffect.NavigateTo(
                                        screen = Screen.PARTICIPATION_METHOD,
                                        clearBackStack = true
                                    )
                                )
                                return@launch
                            }

                            updateState { it.copy(groups = getJoiningGroupsResult.groups) }
                        }.invokeOnCompletion {
                            updateState { it.copy(isLoading = false) }
                        }

                        dismissWithdrawDialog()
                    }
                }
            }

            is GroupManagementUiEvent.Callback -> {
                when (event) {
                    is GroupManagementUiEvent.Callback.OnWithdrawDialogDismissRequested -> {
                        dismissWithdrawDialog()
                    }
                }
            }
        }
    }

    private fun dismissWithdrawDialog() {
        updateState { it.copy(withdrawGroupDialogState = it.withdrawGroupDialogState.copy(isShowing = false)) }
    }
}