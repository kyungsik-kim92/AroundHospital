package com.example.aroundhospital

import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import javax.inject.Singleton

@Singleton
class MapManager(
    private val onMapEventType: (MapViewEventType) -> Unit,
    private val onMapPOIItemEventType: (POIItemEventType) -> Unit
) : MapView.POIItemEventListener, MapView.MapViewEventListener {
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        if (p0 != null && p1 != null) {
            onMapPOIItemEventType(POIItemEventType.Selected(p1))
        }
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

    }

    override fun onMapViewInitialized(p0: MapView?) {

    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
        if (p0 != null && p1 != null) onMapEventType(MapViewEventType.MapCenterChanged(p1))
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {

    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
        if (p1 != null) onMapEventType(MapViewEventType.DragStarted)
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }
}