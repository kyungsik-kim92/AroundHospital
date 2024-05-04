package com.example.aroundhospital.map

import com.example.aroundhospital.base.ViewEvent
import com.example.aroundhospital.response.Document
import com.kakao.vectormap.camera.CameraUpdate

sealed interface MapViewEvent : ViewEvent {
    object ShowProgress : MapViewEvent
    object HideProgress : MapViewEvent

    data class ShowMapPOIItemInfo(val item: Document) : MapViewEvent

    object HideMapPOIItemInfo : MapViewEvent

    data class MoveCamera(val cameraUpdate: CameraUpdate) : MapViewEvent

    data class GetHospitals(val list: List<Document>) : MapViewEvent

}