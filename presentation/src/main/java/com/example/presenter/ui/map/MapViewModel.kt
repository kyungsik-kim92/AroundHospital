package com.example.presenter.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.KakaoMapInfo
import com.example.domain.usecase.DeleteKakaoBookmarkUseCase
import com.example.domain.usecase.GetCurrentLocationUseCase
import com.example.domain.usecase.GetHospitalsUseCase
import com.example.domain.usecase.GetKakaoBookmarkListUseCase
import com.example.domain.usecase.InsertKakaoBookmarkUseCase
import com.example.domain.util.ext.Result
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : ViewModel() {
    private var isLabelClick = false

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MapUiEvent>()
    val uiEvent: SharedFlow<MapUiEvent> = _uiEvent.asSharedFlow()

    private val _isBookmark = MutableStateFlow(false)
    val isBookmarkStateFlow: StateFlow<Boolean> = _isBookmark.asStateFlow()

    private fun isCheckBookmark(document: KakaoMapInfo): Flow<Boolean> {
        return getKakaoBookmarkListUseCase().map { bookmarkList ->
            bookmarkList.any { bookmark ->
                bookmark.id == document.id
            }
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
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isProgressVisible = false)
                }

                Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isProgressVisible = true)
                }

                is Result.Success -> {
                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(result.data.toLatLng())
                    viewModelScope.launch {
                        _uiEvent.emit(MapUiEvent.MoveCamera(cameraUpdate))
                    }

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
                    _uiState.value = _uiState.value.copy(isProgressVisible = true)
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isProgressVisible = false)
                }

                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(isProgressVisible = false)
                    viewModelScope.launch {
                        _uiEvent.emit(MapUiEvent.GetHospitals(result.data.documents))
                    }
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
                (event.label.tag as? KakaoMapInfo)?.let { item ->
                    isLabelClick = true
                    checkBookmarkState(item)
                    val cameraUpdate =
                        CameraUpdateFactory.newCenterPosition(event.label.position)
                    viewModelScope.launch {
                        _uiEvent.emit(MapUiEvent.MoveCamera(cameraUpdate))
                        delay(200L)
                        _uiEvent.emit(MapUiEvent.ShowPOIItem(item))
                    }
                }
            }

            is KakaoMapCameraEvent.MoveEnd -> {
                isLabelClick = false
                flowOf(event.cameraPosition.position).filterNotNull().map {
                    mapCenterChannel.trySend(it)
                }.launchIn(viewModelScope)
            }

            is KakaoMapCameraEvent.MoveStart -> {
                if (!isLabelClick && _uiState.value.isMapInfoVisible) {
                    _uiState.value = _uiState.value.copy(
                        isMapInfoVisible = false,
                        selectedItem = null
                    )
                    _uiEvent.tryEmit(MapUiEvent.HidePOIItem)
                }
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
            _isBookmark.value = it
        }.launchIn(viewModelScope)
    }

    fun moveItem(item: Label?) {
        (item?.tag as? KakaoMapInfo)?.let { mapInfo ->
            checkBookmarkState(mapInfo)
            _uiState.value = _uiState.value.copy(
                isFabVisible = false,
                isMapInfoVisible = true,
                selectedItem = mapInfo
            )
            viewModelScope.launch {
                _uiEvent.emit(MapUiEvent.ShowPOIItem(mapInfo))
            }
        }
    }

    fun updateMapInfoState(isVisible: Boolean, item: KakaoMapInfo?) {
        _uiState.value = _uiState.value.copy(
            isMapInfoVisible = isVisible,
            selectedItem = item
        )
    }

    companion object {
        private const val SEARCH_DEBOUNCE_TIME = 2000L
    }
}



