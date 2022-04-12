package com.malomnogo.exchangerates.ui.dialog.sort

data class SortResult(
    val sortType: SortType = SortType.Alphabet,
    val isAscending: Boolean = true
) {
    companion object {
        val DEFAULT = SortResult()
    }
}

enum class SortType {
    Alphabet,
    Value
}