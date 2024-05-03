package com.example.aroundhospital.map

import com.example.aroundhospital.base.ViewEvent
import com.example.aroundhospital.base.ViewState
import com.example.aroundhospital.response.Document
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint

sealed interface MapViewState : ViewState {
    data class GetPOIItems(val poiItems: Array<MapPOIItem>) : MapViewState

    data class MoveMapCenter(val mapPoint: MapPoint) : MapViewState
}


sealed interface MapViewEvent : ViewEvent {
    object ShowProgress : MapViewEvent
    object HideProgress : MapViewEvent

    data class ShowMapPOIItemInfo(val item: Document) : MapViewEvent

    object HideMapPOIItemInfo : MapViewEvent

    data class MoveMap(val mapPoint: MapPoint) : MapViewEvent

}