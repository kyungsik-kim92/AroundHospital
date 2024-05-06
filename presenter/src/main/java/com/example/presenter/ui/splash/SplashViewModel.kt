package com.example.presenter.ui.splash

import android.Manifest
import android.app.Application
import com.example.domain.util.ext.hasPermission
import com.example.presenter.base.BaseViewModel
import com.example.presenter.ext.LottieAnimateState
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