package com.example.presenter.home


import com.example.domain.model.KakaoMapInfo

sealed interface HomeUiState {
    data object Idle : HomeUiState
    data object Loading : HomeUiState
    data class Error(val message: String) : HomeUiState
}

sealed interface HomeUiEvent {
    data class MoveToMap(val item: KakaoMapInfo) : HomeUiEvent
}