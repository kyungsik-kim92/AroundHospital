package com.example.presenter.ui.map


import com.example.domain.model.KakaoMapInfo
import com.kakao.vectormap.camera.CameraUpdate


data class MapUiState(
    val isProgressVisible: Boolean = false,
    val isFabVisible: Boolean = true,
    val isMapInfoVisible: Boolean = false,
    val selectedItem: KakaoMapInfo? = null
)

sealed interface MapUiEvent {
    data class MoveCamera(val cameraUpdate: CameraUpdate) : MapUiEvent
    data class GetHospitals(val list: List<KakaoMapInfo>) : MapUiEvent
    data class ShowPOIItem(val item: KakaoMapInfo) : MapUiEvent
    data object HidePOIItem : MapUiEvent
}