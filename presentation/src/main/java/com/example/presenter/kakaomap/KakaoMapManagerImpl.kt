package com.example.presenter.kakaomap


import com.example.domain.model.KakaoMapInfo
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class KakaoMapManagerImpl @Inject constructor() : KakaoMapManager {

    private var mapView: MapView? = null
    private var kakaoMap: KakaoMap? = null
    private var labelLayer: LabelLayer? = null

    private val _kakaoMapEventChannel = Channel<KakaoMapEvent>()
    override val kakaoMapEventFlow: Flow<KakaoMapEvent> = _kakaoMapEventChannel.receiveAsFlow()

    private val kakaoMapReadyCallbackImpl = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            this@KakaoMapManagerImpl.kakaoMap = kakaoMap
            this@KakaoMapManagerImpl.kakaoMap?.setOnLabelClickListener(kakaoMapLabelClickListener)
            this@KakaoMapManagerImpl.kakaoMap?.setOnCameraMoveStartListener(
                kakaoMapCameraMoveStartListener
            )
            this@KakaoMapManagerImpl.kakaoMap?.setOnCameraMoveEndListener(
                kakaoMapCameraMoveEndListener
            )
            this@KakaoMapManagerImpl.kakaoMap?.labelManager?.let {
                labelLayer = it.layer
            }
            sendEvent(KakaoMapReadyCallbackEvent.Ready(kakaoMap))
        }
    }

    private val kakaoMapLabelClickListener =
        KakaoMap.OnLabelClickListener { kakaoMap, layer, label ->
            sendEvent(KakaoMapLabelEvent.Click(kakaoMap, layer, label))
            true
        }

    private val kakaoMapLifeCycleCallbackImpl = object : MapLifeCycleCallback() {
        override fun onMapResumed() {
            super.onMapResumed()
            sendEvent(KakaoMapLifeCycleCallbackEvent.Resumed)
        }

        override fun onMapPaused() {
            super.onMapPaused()
            sendEvent(KakaoMapLifeCycleCallbackEvent.Paused)
        }

        override fun onMapDestroy() {
            sendEvent(KakaoMapLifeCycleCallbackEvent.Destroy)
            _kakaoMapEventChannel.close()
        }

        override fun onMapError(error: Exception?) {
            sendEvent(KakaoMapLifeCycleCallbackEvent.Error(error))
        }
    }

    private val kakaoMapCameraMoveEndListener =
        KakaoMap.OnCameraMoveEndListener { kakaoMap, cameraPosition, gestureType ->
            sendEvent(KakaoMapCameraEvent.MoveEnd(kakaoMap, cameraPosition, gestureType))
        }


    private val kakaoMapCameraMoveStartListener =
        KakaoMap.OnCameraMoveStartListener { kakaoMap, gestureType ->
            sendEvent(KakaoMapCameraEvent.MoveStart(kakaoMap, gestureType))
        }

    override fun init(mapView: MapView) {
        this@KakaoMapManagerImpl.mapView = mapView
        this@KakaoMapManagerImpl.mapView?.start(
            kakaoMapLifeCycleCallbackImpl,
            kakaoMapReadyCallbackImpl
        )
    }

    override fun moveCamera(cameraUpdate: CameraUpdate) {
        kakaoMap?.moveCamera(cameraUpdate)
    }

    override fun getLabel(item: KakaoMapInfo): Label? {
        val styles =
            kakaoMap?.labelManager?.addLabelStyles(LabelStyleManager.invoke(LabelStyleType.BASIC))

        return if (labelLayer?.hasLabel(item.id) == true) {
            labelLayer?.getLabel(item.id)
        } else {
            val labelOptions = item.toKakaoMapLabelOption().setStyles(styles)
            labelLayer?.addLabel(labelOptions)
            labelLayer?.getLabel(item.id)
        }
    }

    override fun addLabels(items: List<KakaoMapInfo>) {
        val styles =
            kakaoMap?.labelManager?.addLabelStyles(LabelStyleManager.invoke(LabelStyleType.BASIC))

        items.map { it.toKakaoMapLabelOption() }
        labelLayer?.addLabels(items.map { it.toKakaoMapLabelOption().setStyles(styles) })
    }


    private fun sendEvent(event: KakaoMapEvent) {
        _kakaoMapEventChannel.trySend(event)
    }
}
