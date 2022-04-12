package com.malomnogo.exchangerates.ui.list

import com.malomnogo.exchangerates.utils.ErrorDialogData

sealed class CurrenciesListResult {
    object Loading : CurrenciesListResult()
    data class Success(val result: List<CurrencyItemUiState>) : CurrenciesListResult()
    data class Error(val errorData: ErrorDialogData) : CurrenciesListResult()
}