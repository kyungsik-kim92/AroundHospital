package com.example.presenter.ui.splash


sealed interface SplashUiState {
    data object RouteMap : SplashUiState
    data object RequestPermission : SplashUiState
}