package site.dogether.presentation.base

interface UiEvent {
    sealed interface Click : UiEvent {
        data object OnClickBack : Click
    }
}