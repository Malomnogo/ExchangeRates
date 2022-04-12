package com.malomnogo.exchangerates.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malomnogo.exchangerates.data.CurrenciesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.malomnogo.exchangerates.data.Result
import com.malomnogo.exchangerates.data.local.LocalCurrencyModel
import com.malomnogo.exchangerates.data.mappers.toUiState
import com.malomnogo.exchangerates.ui.dialog.sort.SortResult
import com.malomnogo.exchangerates.ui.dialog.sort.SortType
import com.malomnogo.exchangerates.utils.ErrorDialogData

@HiltViewModel
class ListViewModel
@Inject constructor(private val currenciesRepository: CurrenciesRepository) : ViewModel() {

    private var sortResult: SortResult = SortResult.DEFAULT
    private val _currenciesListResult =
        MutableStateFlow<CurrenciesListResult>(CurrenciesListResult.Loading)
    val currenciesListResult: StateFlow<CurrenciesListResult> = _currenciesListResult

    private val _favoriteCurrenciesResult =
        MutableStateFlow<CurrenciesListResult>(CurrenciesListResult.Loading)
    val favoriteCurrenciesResult: StateFlow<CurrenciesListResult> = _favoriteCurrenciesResult

    private val _favoriteCurrencyChangeResult =
        MutableStateFlow<FavoriteCurrencyResult>(FavoriteCurrencyResult.Default)
    val favoriteCurrencyChangeResult: StateFlow<FavoriteCurrencyResult> =
        _favoriteCurrencyChangeResult

    var lastBase: String = "EUR"
        set(value) {
            getLatestCurrencyRates(field, update = field != value)
            field = value
        }

    private fun getLatestCurrencyRates(
        baseCurrency: String,
        update: Boolean = true
    ) {

        viewModelScope.launch {
            _currenciesListResult.value = CurrenciesListResult.Loading
            val currenciesRequest = async(Dispatchers.IO) {
                currenciesRepository.getCurrencyList(baseCurrency, update)
            }
            val currenciesResponse = currenciesRequest.await()
            when (currenciesResponse.status) {
                Result.Status.ERROR -> handleCurrenciesResultError(currenciesResponse.message)
                Result.Status.SUCCESS -> handleCurrenciesResultSuccess(currenciesResponse.data)
            }
        }
    }

    fun getFavoriteCurrencies() {
        viewModelScope.launch {
            _currenciesListResult.value = CurrenciesListResult.Loading
            val currenciesRequest = async(Dispatchers.IO) {
                currenciesRepository.getFavoriteCurrencyList()
            }
            val currenciesResponse = currenciesRequest.await()
            when (currenciesResponse.status) {
                Result.Status.ERROR -> {
                    _favoriteCurrenciesResult.value =
                        CurrenciesListResult.Error(
                            ErrorDialogData(
                                currenciesResponse.message,
                                retryAction = { getFavoriteCurrencies() }
                            )
                        )
                }
                Result.Status.SUCCESS -> {
                    val resultStates = currenciesResponse.data?.map { currency ->
                        currency.toUiState(::changeFavoriteCurrency)
                    }
                    val sortedStates = sortCurrenciesUiStates(resultStates)
                    _favoriteCurrenciesResult.value = CurrenciesListResult.Success(sortedStates)
                }
            }
        }
    }

    private fun changeFavoriteCurrency(symbol: String, isFavorite: Boolean) {
        viewModelScope.launch {
            _favoriteCurrencyChangeResult.value = FavoriteCurrencyResult.Default

            val changeRequest = async(Dispatchers.IO) {
                currenciesRepository.changeFavoriteCurrency(symbol, isFavorite)
            }

            val changeResponse = changeRequest.await()
            when (changeResponse.status) {
                Result.Status.ERROR -> FavoriteCurrencyResult.Error(
                    ErrorDialogData(
                        changeResponse.message,
                        retryAction = { changeFavoriteCurrency(symbol, isFavorite) })
                )
                Result.Status.SUCCESS -> {
                    if (changeResponse.data != null) {
                        val uiState = changeResponse.data.toUiState(::changeFavoriteCurrency)
                        _favoriteCurrencyChangeResult.value =
                            FavoriteCurrencyResult.Success(uiState)
                    }
                }
            }
        }
    }

    private fun handleCurrenciesResultError(errorMessage: String?) {
        _currenciesListResult.value = CurrenciesListResult.Error(
            ErrorDialogData(
                errorMessage,
                retryAction = {
                    getLatestCurrencyRates(lastBase)
                }
            )
        )
    }

    private fun handleCurrenciesResultSuccess(result: List<LocalCurrencyModel>?) {
        val resultStates = result?.map { currency ->
            currency.toUiState(::changeFavoriteCurrency)
        }
        val sortedStates = sortCurrenciesUiStates(resultStates)
        _currenciesListResult.value = CurrenciesListResult.Success(sortedStates)
    }

    private fun sortCurrenciesUiStates(states: List<CurrencyItemUiState>?): List<CurrencyItemUiState> {
        var sortedStates = when (sortResult.sortType) {
            SortType.Alphabet -> {
                states?.sortedBy { it.symbol }
            }
            SortType.Value -> {
                states?.sortedBy { it.rate }
            }
        }

        if (!sortResult.isAscending) {
            sortedStates = sortedStates?.reversed()
        }

        return sortedStates!!
    }

    fun changeSortWay(sortResult: SortResult) {
        this.sortResult = sortResult
    }
}