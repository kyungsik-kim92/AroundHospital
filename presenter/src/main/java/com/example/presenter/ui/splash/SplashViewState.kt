package com.example.presenter.ui.splash

import com.example.presenter.base.ViewState


sealed interface SplashViewState : ViewState {
    object RouteMap : SplashViewState
    object RequestPermission : SplashViewState
}