package com.example.presenter.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetKakaoBookmarkListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getKakaoBookmarkListUseCase: GetKakaoBookmarkListUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<BookmarkUiState>(BookmarkUiState.Loading)
    val uiState: StateFlow<BookmarkUiState> = _uiState.asStateFlow()

    init {
        loadBookmarkList()
    }

    private fun loadBookmarkList() {
        _uiState.value = BookmarkUiState.Loading

        getKakaoBookmarkListUseCase()
            .onEach { bookmarkList ->
                _uiState.value = BookmarkUiState.Success(bookmarkList)
            }
            .launchIn(viewModelScope)
    }

}