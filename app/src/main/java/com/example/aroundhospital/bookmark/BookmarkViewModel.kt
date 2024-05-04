package com.example.aroundhospital.bookmark

import androidx.lifecycle.viewModelScope
import com.example.aroundhospital.base.BaseViewModel
import com.example.aroundhospital.data.repo.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class BookmarkViewModel @Inject constructor(bookmarkRepository: BookmarkRepository) :
    BaseViewModel() {

    val isVisibleListStateFlow = MutableStateFlow(true)

    init {
        bookmarkRepository.getAll.map {
            isVisibleListStateFlow.value = it.isNotEmpty()
            onChangedViewEvent(BookmarkViewEvent.GetBookmarkList(it))
        }.launchIn(viewModelScope)
    }


}