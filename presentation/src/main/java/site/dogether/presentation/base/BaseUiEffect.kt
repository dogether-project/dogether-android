package site.dogether.presentation.base

import site.dogether.presentation.screen.error.model.Error

interface BaseUiEffect {
    data object NavigateToBack : BaseUiEffect

    data class NavigateToError(val error: Error) : BaseUiEffect
}