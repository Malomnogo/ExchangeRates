package com.malomnogo.exchangerates.ui.dialog.sort

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SortViewModel @Inject constructor() : ViewModel() {

    private val _sortWayResult = MutableStateFlow(SortResult.DEFAULT)
    val sortResult: StateFlow<SortResult> = _sortWayResult

    fun changeSortWay(sortType: SortType, isAscending: Boolean) {
        _sortWayResult.value = SortResult(sortType, isAscending)
    }
}