package com.example.presenter.home



import com.example.domain.model.KakaoMapInfo
import com.example.presenter.base.ViewEvent

sealed interface HomeViewEvent : ViewEvent {
    data class MoveItem(val item: KakaoMapInfo) : HomeViewEvent
}

