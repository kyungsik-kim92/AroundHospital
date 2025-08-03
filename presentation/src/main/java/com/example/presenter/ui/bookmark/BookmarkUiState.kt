package com.example.presenter.ui.bookmark


import com.example.domain.model.KakaoMapInfo


sealed interface BookmarkViewEvent {
    data class GetBookmarkList(val bookmarkList: List<KakaoMapInfo>) : BookmarkViewEvent
}