package com.example.presenter.ui.splash

import android.Manifest
import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.domain.util.ext.hasPermission
import com.example.presenter.ext.LottieAnimateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val app: Application) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState?>(null)
    val uiState: StateFlow<SplashUiState?> = _uiState.asStateFlow()

    val animateState: Function1<LottieAnimateState, Unit> = ::onAnimationState

    private fun onAnimationState(state: LottieAnimateState) {
        when (state) {
            LottieAnimateState.Start -> {}
            LottieAnimateState.End -> {
                val permissionApproved =
                    app.applicationContext.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                if (permissionApproved) {
                    _uiState.value = SplashUiState.RouteMap
                } else {
                    _uiState.value = SplashUiState.RequestPermission
                }
            }

            LottieAnimateState.Cancel -> {}
            LottieAnimateState.Repeat -> {}
        }
    }
}