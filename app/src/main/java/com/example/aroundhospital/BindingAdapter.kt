package com.example.aroundhospital

import android.animation.Animator
import android.view.View
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

@BindingAdapter("onLottieAnimateState")
fun LottieAnimationView.onLottieAnimateState(state: Function1<LottieAnimateState, Unit>?) {
    this.addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            state?.invoke(LottieAnimateState.Start)
        }

        override fun onAnimationEnd(animation: Animator) {
            state?.invoke(LottieAnimateState.End)
        }

        override fun onAnimationCancel(animation: Animator) {
            state?.invoke(LottieAnimateState.Cancel)
        }

        override fun onAnimationRepeat(animation: Animator) {
            state?.invoke(LottieAnimateState.Repeat)
        }
    })
}


sealed interface LottieAnimateState {
    object Start : LottieAnimateState
    object End : LottieAnimateState
    object Cancel : LottieAnimateState
    object Repeat : LottieAnimateState
}

@BindingAdapter("onPOIItemEventListener")
fun MapView.onPOIItemEventListener(f: Function1<POIItemEventType, Unit>?) {
    setPOIItemEventListener(object : MapView.POIItemEventListener {
        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
            f?.invoke(POIItemEventType.Selected(p1))
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
    })
}

sealed interface POIItemEventType {
    data class Selected(val item: MapPOIItem?) : POIItemEventType
}


@BindingAdapter("onMapViewEventListener")
fun MapView.onMapViewEventListener(f: Function1<MapViewEventType, Unit>?) {

    val eventListener = object : MapView.MapViewEventListener {
        override fun onMapViewInitialized(p0: MapView?) {
        }

        override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {

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
            f?.invoke(MapViewEventType.DragStarted)
        }

        override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
        }
    }

    setMapViewEventListener(eventListener)
}

sealed interface MapViewEventType {

    object DragStarted : MapViewEventType

    data class MapCenterChanged(val mapPoint: MapPoint) : MapViewEventType
}


@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}
