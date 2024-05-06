package com.example.presenter.kakaomap

import com.example.data.api.response.Document
import com.example.domain.model.KakaoMapInfo
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.label.Label
import kotlinx.coroutines.flow.Flow

interface KakaoMapManager {

    val kakaoMapEventFlow: Flow<KakaoMapEvent>
    fun init(mapView: MapView)
    fun addLabels(items: List<KakaoMapInfo>)
    fun moveCamera(cameraUpdate: CameraUpdate)
    fun getLabel(item: KakaoMapInfo): Label?
}
