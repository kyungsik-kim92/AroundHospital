package com.example.aroundhospital.ui.bookmark

import com.example.aroundhospital.base.ViewEvent
import com.example.aroundhospital.base.ViewState
import com.example.data.api.response.Document
import com.example.domain.model.KakaoMapInfo

class BookmarkViewState : ViewState {
}

sealed interface BookmarkViewEvent : ViewEvent {

    data class GetBookmarkList(val bookmarkList: List<KakaoMapInfo>) : BookmarkViewEvent
}