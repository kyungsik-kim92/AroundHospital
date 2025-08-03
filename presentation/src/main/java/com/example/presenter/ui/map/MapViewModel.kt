package com.example.presenter.ui.map

import androidx.lifecycle.viewModelScope
import com.example.domain.model.KakaoMapInfo
import com.example.domain.usecase.DeleteKakaoBookmarkUseCase
import com.example.domain.usecase.GetCurrentLocationUseCase
import com.example.domain.usecase.GetHospitalsUseCase
import com.example.domain.usecase.GetKakaoBookmarkListUseCase
import com.example.domain.usecase.InsertKakaoBookmarkUseCase
import com.example.domain.util.ext.Result
import com.example.presenter.base.BaseViewModel
import com.example.presenter.kakaomap.KakaoMapCameraEvent
import com.example.presenter.kakaomap.KakaoMapEvent
import com.example.presenter.kakaomap.KakaoMapLabelEvent
import com.example.presenter.kakaomap.KakaoMapLifeCycleCallbackEvent
import com.example.presenter.kakaomap.KakaoMapReadyCallbackEvent
import com.example.presenter.kakaomap.toLatLng
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val getHospitalsUseCase: GetHospitalsUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getKakaoBookmarkListUseCase: GetKakaoBookmarkListUseCase,
    private val insertKakaoBookmarkUseCase: InsertKakaoBookmarkUseCase,
    private val deleteKakaoBookmarkUseCase: DeleteKakaoBookmarkUseCase
) : BaseViewModel() {

    val isBookmarkStateFlow = MutableStateFlow(false)

    private val isCheckBookmark: (KakaoMapInfo) -> Flow<Boolean> = { document ->
        getKakaoBookmarkListUseCase().map {
            it.any { bookmark -> bookmark.id == document.id }
        }
    }

    private val mapCenterChannel =
        Channel<LatLng>(capacity = Channel.UNLIMITED, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        mapCenterChannel.receiveAsFlow().debounce(SEARCH_DEBOUNCE_TIME).map(::search)
            .launchIn(viewModelScope)

    }

    fun moveCurrentLocation() {
        getCurrentLocationUseCase().onEach { result ->
            when (result) {
                is Result.Error -> Unit
                Result.Loading -> onChangedViewEvent(MapUiEvent.ShowProgress)
                is Result.Success -> {
                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(result.data.toLatLng())
                    onChangedViewEvent(MapUiEvent.MoveCamera(cameraUpdate))
                    onChangedViewEvent(MapUiEvent.HideProgress)

                }
            }
        }.launchIn(viewModelScope)
    }


    private fun search(latLng: LatLng) {
        getHospitalsUseCase(
            latLng.longitude.toString(),
            latLng.latitude.toString()
        ).onEach { result ->
            when (result) {
                Result.Loading -> {
                    onChangedViewEvent(MapUiEvent.ShowProgress)
                }

                is Result.Error -> {

                }

                is Result.Success -> {
                    onChangedViewEvent(MapUiEvent.GetHospitals(result.data.documents))
                    onChangedViewEvent(MapUiEvent.HideProgress)
                }
            }
        }.launchIn(viewModelScope)
    }


    fun toggleBookmark(item: KakaoMapInfo) = viewModelScope.launch(Dispatchers.IO) {
        if (isCheckBookmark(item).first()) {
            deleteKakaoBookmarkUseCase(item)
        } else {
            insertKakaoBookmarkUseCase(item)
        }
    }

    fun kakaoMapEvent(event: KakaoMapEvent) {
        when (event) {
            is KakaoMapLifeCycleCallbackEvent -> processKakaoMapLifecycleEvent(event)
            is KakaoMapReadyCallbackEvent.Ready -> {
                moveCurrentLocation()
            }

            is KakaoMapLabelEvent.Click -> {
                (event.label.tag as? KakaoMapInfo)?.let {
                    checkBookmarkState(it)
                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(event.label.position)
                    onChangedViewEvent(MapUiEvent.MoveCamera(cameraUpdate))
                    onChangedViewEvent(MapUiEvent.ShowMapPOIItemInfo(it))
                }
            }

            is KakaoMapCameraEvent.MoveEnd -> {
                flowOf(event.cameraPosition.position).filterNotNull().map {
                    mapCenterChannel.trySend(it)
                }.launchIn(viewModelScope)
            }

            is KakaoMapCameraEvent.MoveStart -> {
                isBookmarkStateFlow.value = false
                onChangedViewEvent(MapUiEvent.HideMapPOIItemInfo)
            }
        }
    }

    private fun processKakaoMapLifecycleEvent(event: KakaoMapLifeCycleCallbackEvent) {
        when (event) {
            KakaoMapLifeCycleCallbackEvent.Destroy -> {}
            is KakaoMapLifeCycleCallbackEvent.Error -> {}
            KakaoMapLifeCycleCallbackEvent.Paused -> {}
            KakaoMapLifeCycleCallbackEvent.Resumed -> {}
        }
    }


    private fun checkBookmarkState(item: KakaoMapInfo) {
        isCheckBookmark(item).onEach {
            isBookmarkStateFlow.value = it
        }.launchIn(viewModelScope)
    }

    fun moveItem(item: Label?) {
        (item?.tag as? KakaoMapInfo)?.let {
            checkBookmarkState(it)
            onChangedViewEvent(MapUiEvent.ShowMapPOIItemInfo(it))
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_TIME = 2000L
    }
}



