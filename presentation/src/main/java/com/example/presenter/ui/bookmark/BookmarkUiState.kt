package com.example.presenter.ui.bookmark


import com.example.domain.model.KakaoMapInfo


sealed interface BookmarkUiState {
    data object Loading : BookmarkUiState
    data class Success(val bookmarkList: List<KakaoMapInfo>) : BookmarkUiState
    data class Error(val message: String) : BookmarkUiState
}