package com.example.aroundhospital.ui.splash

import com.example.aroundhospital.base.ViewState

sealed interface SplashViewState : ViewState {
    object RouteMap : SplashViewState
    object RequestPermission : SplashViewState
}