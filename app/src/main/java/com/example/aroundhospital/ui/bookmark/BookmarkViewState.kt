package com.example.aroundhospital.ui.bookmark

import com.example.aroundhospital.base.ViewEvent
import com.example.aroundhospital.base.ViewState
import com.example.data.api.response.Document

class BookmarkViewState : ViewState {
}

sealed interface BookmarkViewEvent : ViewEvent {

    data class GetBookmarkList(val bookmarkList: List<Document>) : BookmarkViewEvent
}