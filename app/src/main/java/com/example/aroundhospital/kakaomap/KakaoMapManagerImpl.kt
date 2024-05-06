package com.example.aroundhospital.kakaomap

import com.example.aroundhospital.LabelStyleManager
import com.example.aroundhospital.LabelStyleType
import com.example.aroundhospital.base.BaseCoroutineScope
import com.example.domain.model.KakaoMapInfo
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class KakaoMapManagerImpl @Inject constructor() : KakaoMapManager, BaseCoroutineScope() {

    private var mapView: MapView? = null
    private var kakaoMap: KakaoMap? = null
    private var labelLayer: LabelLayer? = null

    private val _kakaoMapEventChannel = Channel<KakaoMapEvent>()
    override val kakaoMapEventFlow: Flow<KakaoMapEvent> = _kakaoMapEventChannel.receiveAsFlow()

    private val kakaoMapReadyCallbackImpl = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            this@KakaoMapManagerImpl.kakaoMap = kakaoMap
            this@KakaoMapManagerImpl.kakaoMap?.setOnLabelClickListener(kakaoMapLableClickListener)
            this@KakaoMapManagerImpl.kakaoMap?.setOnCameraMoveStartListener(
                kakaoMapCameraMoveStartListener
            )
            this@KakaoMapManagerImpl.kakaoMap?.setOnCameraMoveEndListener(
                kakaoMapCameraMoveEndListener
            )
            this@KakaoMapManagerImpl.kakaoMap?.labelManager?.let {
                labelLayer = it.layer
            }
            onChagnedViewEvent(KakaoMapReadyCallbackEvent.Ready(kakaoMap))
        }
    }

    private val kakaoMapLableClickListener =
        KakaoMap.OnLabelClickListener { kakaoMap, layer, label ->
            onChagnedViewEvent(KakaoMapLabelEvent.Click(kakaoMap, layer, label))
        }

    private val kakaoMapLifeCycleCallbackImpl = object : MapLifeCycleCallback() {
        override fun onMapResumed() {
            super.onMapResumed()
            onChagnedViewEvent(KakaoMapLifeCycleCallbackEvent.Resumed)
        }

        override fun onMapPaused() {
            super.onMapPaused()
            onChagnedViewEvent(KakaoMapLifeCycleCallbackEvent.Paused)
        }

        override fun onMapDestroy() {
            onChagnedViewEvent(KakaoMapLifeCycleCallbackEvent.Destroy)
            _kakaoMapEventChannel.close()
        }

        override fun onMapError(error: Exception?) {
            onChagnedViewEvent(KakaoMapLifeCycleCallbackEvent.Error(error))
        }
    }

    private val kakaoMapCameraMoveEndListener =
        KakaoMap.OnCameraMoveEndListener { kakaoMap, cameraPosition, gestureType ->
            onChagnedViewEvent(KakaoMapCameraEvent.MoveEnd(kakaoMap, cameraPosition, gestureType))
        }


    private val kakaoMapCameraMoveStartListener =
        KakaoMap.OnCameraMoveStartListener { kakaoMap, gestureType ->
            onChagnedViewEvent(KakaoMapCameraEvent.MoveStart(kakaoMap, gestureType))
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


    private fun onChagnedViewEvent(event: KakaoMapEvent) = launch {
        _kakaoMapEventChannel
            .trySend(event)
            .onSuccess { }
            .onFailure { it?.let(::handleException) }
            .onClosed { it?.let(::handleException) }
    }

    override fun handleException(exception: Throwable) {
        super.handleException(exception)
        onChagnedViewEvent(KakaoMapLifeCycleCallbackEvent.Error(exception as? Exception))
    }
}
