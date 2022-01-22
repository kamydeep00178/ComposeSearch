package com.base.composesearch

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DogViewModel @Inject constructor() : ViewModel() {

    private val _dogListData = MutableStateFlow<List<DogModel>>(emptyList())
    val dogListData = _dogListData.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun onSearchTextChange(value: String) {
        _searchText.value = value
    }

    fun getData() {
        //get data from remote or local
        _dogListData.value = dogList
    }
}