package com.malomnogo.exchangerates.ui.list

import com.malomnogo.exchangerates.utils.ErrorDialogData

sealed class FavoriteCurrencyResult {
    object Default : FavoriteCurrencyResult()
    data class Success(val newItemUiState: CurrencyItemUiState) : FavoriteCurrencyResult()
    data class Error(val errorData: ErrorDialogData) : FavoriteCurrencyResult()
}
