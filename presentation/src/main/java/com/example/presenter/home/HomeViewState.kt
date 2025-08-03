package com.example.presenter.home


import com.example.domain.model.KakaoMapInfo

sealed interface HomeUiEvent {
    data class MoveItem(val item: KakaoMapInfo) : HomeUiEvent
}

