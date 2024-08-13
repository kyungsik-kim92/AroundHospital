package com.example.presenter.ui.map


import com.example.domain.model.KakaoMapInfo
import com.example.presenter.base.ViewEvent
import com.kakao.vectormap.camera.CameraUpdate

sealed interface MapViewEvent : ViewEvent {
    object ShowProgress : MapViewEvent
    object HideProgress : MapViewEvent

    data class ShowMapPOIItemInfo(val item: KakaoMapInfo) : MapViewEvent

    object HideMapPOIItemInfo : MapViewEvent

    data class MoveCamera(val cameraUpdate: CameraUpdate) : MapViewEvent

    data class GetHospitals(val list: List<KakaoMapInfo>) : MapViewEvent

}