package com.example.aroundhospital.domain.manager

import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer

sealed interface KakaoMapEvent


sealed interface KakaoMapLifeCycleCallbackEvent : KakaoMapEvent {
    object Resumed : KakaoMapLifeCycleCallbackEvent
    object Paused : KakaoMapLifeCycleCallbackEvent
    object Destroy : KakaoMapLifeCycleCallbackEvent
    data class Error(val error: Exception?) : KakaoMapLifeCycleCallbackEvent
}


sealed interface KakaoMapReadyCallbackEvent : KakaoMapEvent {
    data class Ready(val kakaoMap: KakaoMap) : KakaoMapReadyCallbackEvent
}

sealed interface KakaoMapLabelEvent : KakaoMapEvent {

    data class Click(val kakaoMap: KakaoMap, val layer: LabelLayer, val label: Label) :
        KakaoMapLabelEvent

}

sealed interface KakaoMapCameraEvent : KakaoMapEvent {

    data class MoveStart(
        val kakaoMap: KakaoMap,
        val gestureType: GestureType
    ) : KakaoMapCameraEvent


    data class MoveEnd(
        val kakaoMap: KakaoMap,
        val cameraPosition: CameraPosition,
        val gestureType: GestureType
    ) : KakaoMapCameraEvent

}