package com.example.presenter.ui.map


import com.example.domain.model.KakaoMapInfo
import com.kakao.vectormap.camera.CameraUpdate

sealed interface MapUiEvent {
    data object ShowProgress : MapUiEvent
    data object HideProgress : MapUiEvent

    data class ShowMapPOIItemInfo(val item: KakaoMapInfo) : MapUiEvent

    data object HideMapPOIItemInfo : MapUiEvent

    data class MoveCamera(val cameraUpdate: CameraUpdate) : MapUiEvent

    data class GetHospitals(val list: List<KakaoMapInfo>) : MapUiEvent

}