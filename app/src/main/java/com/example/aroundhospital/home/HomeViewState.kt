package com.example.aroundhospital.home

import com.example.aroundhospital.base.ViewEvent
import com.example.data.api.response.Document
import com.example.domain.model.KakaoMapInfo

//class HomeViewState {
//}


sealed interface HomeViewEvent : ViewEvent {
    data class MoveItem(val item: KakaoMapInfo) : HomeViewEvent
}

