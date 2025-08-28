package site.dogether.presentation.base

interface BaseUiEvent {
    sealed interface Click {
        data object OnClickBack : Click
    }
}