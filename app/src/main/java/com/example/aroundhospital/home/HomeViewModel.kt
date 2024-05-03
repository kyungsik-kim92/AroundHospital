package com.example.aroundhospital.home

import androidx.lifecycle.viewModelScope
import com.example.aroundhospital.base.BaseViewModel
import com.example.aroundhospital.response.Document
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {
    fun moveItem(item: Document) = viewModelScope.launch {
        onChangedViewEvent(HomeViewEvent.MoveItem(item))
    }

}