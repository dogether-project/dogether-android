package site.dogether.presentation.base

import site.dogether.presentation.Screen
import site.dogether.presentation.screen.error.model.Error

interface UiEffect {
    data object NavigateToPreviousScreen : UiEffect

    data class NavigateTo(val screen: String) : UiEffect

    data class NavigateToError(val error: Error) : UiEffect
}