package com.example.aroundhospital.domain.manager

import com.example.aroundhospital.response.Document
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.label.Label
import kotlinx.coroutines.flow.Flow

interface KakaoMapManager {

    val kakaoMapEventFlow: Flow<KakaoMapEvent>
    fun init(mapView: MapView)
    fun addLabels(items: List<Document>)
    fun moveCamera(cameraUpdate: CameraUpdate)
    fun getLabel(item: Document): Label?
}
