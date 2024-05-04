package com.example.aroundhospital.map

import androidx.lifecycle.viewModelScope
import com.example.aroundhospital.Result
import com.example.aroundhospital.base.BaseViewModel
import com.example.aroundhospital.data.repo.BookmarkRepository
import com.example.aroundhospital.domain.manager.KakaoMapCameraEvent
import com.example.aroundhospital.domain.manager.KakaoMapEvent
import com.example.aroundhospital.domain.manager.KakaoMapLabelEvent
import com.example.aroundhospital.domain.manager.KakaoMapLifeCycleCallbackEvent
import com.example.aroundhospital.domain.manager.KakaoMapReadyCallbackEvent
import com.example.aroundhospital.domain.usecase.GetCurrentLocationUseCase
import com.example.aroundhospital.domain.usecase.GetHospitalsUseCase
import com.example.aroundhospital.response.Document
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
    private val bookmarkRepository: BookmarkRepository
) : BaseViewModel() {

    val isBookmarkStateFlow = MutableStateFlow(false)

    private val isCheckBookmark: (Document) -> Flow<Boolean> = { document ->
        bookmarkRepository.getAll.map {
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
                Result.Loading -> {
                    onChangedViewEvent(MapViewEvent.ShowProgress)
                }

                is Result.Error -> {

                }

                is Result.Success -> {
                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(result.data)
                    onChangedViewEvent(MapViewEvent.MoveCamera(cameraUpdate))
                    onChangedViewEvent(MapViewEvent.HideProgress)
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
                    onChangedViewEvent(MapViewEvent.ShowProgress)
                }

                is Result.Error -> {

                }

                is Result.Success -> {
                    onChangedViewEvent(MapViewEvent.GetHospitals(result.data))
                    onChangedViewEvent(MapViewEvent.HideProgress)
                }
            }
        }.launchIn(viewModelScope)
    }


    fun toggleBookmark(item: Document) = viewModelScope.launch(Dispatchers.IO) {
        if (isCheckBookmark(item).first()) {
            bookmarkRepository.delete(item)
        } else {
            bookmarkRepository.insert(item)
        }
    }

    fun kakaoMapEvent(event: KakaoMapEvent) {
        when (event) {
            is KakaoMapLifeCycleCallbackEvent -> processKakaMapLifecycleEvent(event)
            is KakaoMapReadyCallbackEvent.Ready -> {
                moveCurrentLocation()
            }

            is KakaoMapLabelEvent.Click -> {
                (event.label.tag as? Document)?.let {
                    checkBookmarkState(it)
                    onChangedViewEvent(MapViewEvent.ShowMapPOIItemInfo(it))
                }
            }

            is KakaoMapCameraEvent.MoveEnd -> {
                flowOf(event.cameraPosition.position).filterNotNull().map {
                    mapCenterChannel.trySend(it)
                }.launchIn(viewModelScope)
            }

            is KakaoMapCameraEvent.MoveStart -> {
                isBookmarkStateFlow.value = false
                onChangedViewEvent(MapViewEvent.HideMapPOIItemInfo)
            }
        }
    }

    private fun processKakaMapLifecycleEvent(event: KakaoMapLifeCycleCallbackEvent) {
        when (event) {
            KakaoMapLifeCycleCallbackEvent.Destroy -> {}
            is KakaoMapLifeCycleCallbackEvent.Error -> {}
            KakaoMapLifeCycleCallbackEvent.Paused -> {}
            KakaoMapLifeCycleCallbackEvent.Resumed -> {}
        }
    }


    private fun checkBookmarkState(item: Document) {
        isCheckBookmark(item).onEach {
            isBookmarkStateFlow.value = it
        }.launchIn(viewModelScope)
    }

    fun moveItem(item: Label?) {
        (item?.tag as? Document)?.let {
            checkBookmarkState(it)
            onChangedViewEvent(MapViewEvent.ShowMapPOIItemInfo(it))
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_TIME = 2000L
    }
}

