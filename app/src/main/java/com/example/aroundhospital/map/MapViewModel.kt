package com.example.aroundhospital.map

import androidx.lifecycle.viewModelScope
import com.example.aroundhospital.MapViewEventType
import com.example.aroundhospital.MapXY
import com.example.aroundhospital.POIItemEventType
import com.example.aroundhospital.Result
import com.example.aroundhospital.base.BaseViewModel
import com.example.aroundhospital.data.repo.BookmarkRepository
import com.example.aroundhospital.response.Document
import com.example.aroundhospital.response.toMapPOIItem
import com.example.aroundhospital.toMapXY
import com.example.aroundhospital.usecase.GetCurrentLocationUseCase
import com.example.aroundhospital.usecase.GetHospitalsUseCase
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
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val getHospitalsUseCase: GetHospitalsUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val bookmarkRepository: BookmarkRepository
) : BaseViewModel() {

    val isBookmarkStateFlow = MutableStateFlow(false)

    private var currentMapCenterPoint: MapPoint? = null

    private val cacheMapPOIItemList = mutableSetOf<MapPOIItem>()

    private val isCheckBookmark: (Document) -> Flow<Boolean> = { document ->
        bookmarkRepository.getAll.map {
            it.any { bookmark -> bookmark.id == document.id }
        }
    }

    private val mapCenterChannel =
        Channel<MapXY>(capacity = Channel.UNLIMITED, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        moveCurrentLocation()
        mapCenterChannel.receiveAsFlow().debounce(SEARCH_DEBOUNCE_TIME).map {
            search(it.x, it.y)
        }.launchIn(viewModelScope)


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
                    currentMapCenterPoint = result.data
                    onChangedViewEvent(MapViewEvent.HideProgress)
                    onChangedViewState(MapViewState.MoveMapCenter(result.data))
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun search(x: String, y: String) {
        getHospitalsUseCase(x, y).onEach { result ->
            when (result) {
                Result.Loading -> {
                    onChangedViewEvent(MapViewEvent.ShowProgress)
                }

                is Result.Error -> {

                }

                is Result.Success -> {

                    val newItemList = result.data.filter {
                        it.id !in cacheMapPOIItemList.map { it.userObject as Document }
                            .map { it.id }
                    }.map { it.toMapPOIItem() }

                    cacheMapPOIItemList.addAll(newItemList)
                    onChangedViewEvent(MapViewEvent.HideProgress)
                    onChangedViewState(MapViewState.GetPOIItems(newItemList.toTypedArray()))
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

    fun processMapViewEvent(type: MapViewEventType) {
        when (type) {
            MapViewEventType.DragStarted -> {
                onChangedViewEvent(MapViewEvent.HideMapPOIItemInfo)
            }

            is MapViewEventType.MapCenterChanged -> {
                flowOf(type.mapPoint).filterNotNull().map {
                    mapCenterChannel.trySend(it.toMapXY())
                }.launchIn(viewModelScope)
            }
        }
    }

    fun processPOIItemEvent(type: POIItemEventType) {
        when (type) {
            is POIItemEventType.Selected -> {
                (type.item?.userObject as? Document)?.let {
                    checkBookmarkState(it)
                    onChangedViewEvent(MapViewEvent.ShowMapPOIItemInfo(it))
                }
            }
        }
    }

    private fun checkBookmarkState(item: Document) {
        isCheckBookmark(item).onEach {
            isBookmarkStateFlow.value = it
        }.launchIn(viewModelScope)
    }

    fun moveItem(item: Document) {
        if (item in cacheMapPOIItemList.map { it.userObject as Document }) {
            onChangedViewEvent(MapViewEvent.MoveMap(item.toMapPOIItem().mapPoint))
            processPOIItemEvent(POIItemEventType.Selected(item.toMapPOIItem()))
        } else {
            onChangedViewEvent(MapViewEvent.MoveMap(item.toMapPOIItem().mapPoint))
            cacheMapPOIItemList.add(item.toMapPOIItem())
            onChangedViewState(MapViewState.GetPOIItems(arrayOf(item.toMapPOIItem())))
            processPOIItemEvent(POIItemEventType.Selected(item.toMapPOIItem()))
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_TIME = 1000L
    }
}

