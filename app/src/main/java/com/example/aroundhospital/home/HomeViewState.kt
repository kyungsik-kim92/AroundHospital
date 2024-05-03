package com.example.aroundhospital.home

import com.example.aroundhospital.base.ViewEvent
import com.example.aroundhospital.response.Document

//class HomeViewState {
//}


sealed interface HomeViewEvent : ViewEvent {
    data class MoveItem(val item: Document) : HomeViewEvent
}

