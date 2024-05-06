package com.example.presenter.home

import androidx.lifecycle.viewModelScope
import com.example.domain.model.KakaoMapInfo
import com.example.presenter.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {
    fun moveItem(item: KakaoMapInfo) = viewModelScope.launch {
        onChangedViewEvent(HomeViewEvent.MoveItem(item))
    }

}