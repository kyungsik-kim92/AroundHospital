package com.example.presenter.ui.bookmark


import com.example.domain.model.KakaoMapInfo
import com.example.presenter.base.ViewEvent
import com.example.presenter.base.ViewState

class BookmarkViewState : ViewState {
}

sealed interface BookmarkViewEvent : ViewEvent {

    data class GetBookmarkList(val bookmarkList: List<KakaoMapInfo>) : BookmarkViewEvent
}