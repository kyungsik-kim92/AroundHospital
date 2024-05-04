package com.example.aroundhospital.bookmark

import com.example.aroundhospital.base.ViewEvent
import com.example.aroundhospital.base.ViewState
import com.example.aroundhospital.response.Document

class BookmarkViewState : ViewState {
}

sealed interface BookmarkViewEvent : ViewEvent {

    data class GetBookmarkList(val bookmarkList: List<Document>) : BookmarkViewEvent
}