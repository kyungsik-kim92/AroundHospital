package com.example.aroundhospital.ui.splash

import android.Manifest
import android.app.Application
import com.example.aroundhospital.LottieAnimateState
import com.example.aroundhospital.base.BaseViewModel
import com.example.domain.util.ext.hasPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val app: Application) : BaseViewModel() {

    val animateState: Function1<LottieAnimateState, Unit> = ::onAnimationState


    private fun onAnimationState(state: LottieAnimateState) {
        when (state) {
            LottieAnimateState.Start -> {}
            LottieAnimateState.End -> {
                val permissionApproved =
                    app.applicationContext.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                if (permissionApproved) {
                    onChangedViewState(SplashViewState.RouteMap)
                } else {
                    onChangedViewState(SplashViewState.RequestPermission)
                }
            }

            LottieAnimateState.Cancel -> {}
            LottieAnimateState.Repeat -> {}
        }
    }


}