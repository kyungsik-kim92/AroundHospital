package com.example.presenter.ui.bookmark

import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetKakaoBookmarkListUseCase
import com.example.presenter.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class BookmarkViewModel @Inject constructor(getKakaoBookmarkListUseCase: GetKakaoBookmarkListUseCase) :
    BaseViewModel() {

    val isVisibleListStateFlow = MutableStateFlow(true)

    init {
        getKakaoBookmarkListUseCase().map {
            isVisibleListStateFlow.value = it.isNotEmpty()
            onChangedViewEvent(BookmarkViewEvent.GetBookmarkList(it))
        }.launchIn(viewModelScope)
    }


}