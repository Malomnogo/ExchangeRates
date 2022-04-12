package com.malomnogo.exchangerates.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.malomnogo.exchangerates.databinding.FragmentListBinding
import com.malomnogo.exchangerates.ui.dialog.sort.SortDialogFragment
import com.malomnogo.exchangerates.ui.dialog.sort.SortViewModel
import com.malomnogo.exchangerates.ui.dialog.symbol.SymbolsDialogFragment
import com.malomnogo.exchangerates.utils.ErrorDialog
import com.malomnogo.exchangerates.utils.ErrorDialogData
import com.malomnogo.exchangerates.utils.setVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {

    private val viewModel by activityViewModels<ListViewModel>()
    private val sortViewModel by activityViewModels<SortViewModel>()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CurrenciesAdapter
    private var showOnlyFavorites: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showOnlyFavorites = arguments?.getBoolean("isFavorites") ?: false
        initUi()
        observeData()
    }

    private fun initUi() {
        adapter = CurrenciesAdapter()
        with(binding) {
            tvTitle.text = viewModel.lastBase
            rvCurrencies.adapter = adapter
            btnSort.setOnClickListener {
                SortDialogFragment().show(
                    parentFragmentManager,
                    SortDialogFragment::class.java.simpleName
                )
            }
            tvTitle.setOnClickListener {
                SymbolsDialogFragment {
                    tvTitle.text = it
                    viewModel.lastBase = it
                }.show(childFragmentManager, SymbolsDialogFragment::class.java.simpleName)
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (showOnlyFavorites) {
                    viewModel.favoriteCurrenciesResult.collect(::handleCurrenciesListResult)
                } else {
                    viewModel.currenciesListResult.collect(::handleCurrenciesListResult)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.favoriteCurrencyChangeResult.collect(::handleFavoriteCurrencyResult)
        }

        lifecycleScope.launch {
            sortViewModel.sortResult.collect { result ->
                viewModel.changeSortWay(result)
                updateList()
            }
        }
    }

    private fun updateList() {
        if (showOnlyFavorites) {
            viewModel.getFavoriteCurrencies()
        } else {
            viewModel.lastBase = binding.tvTitle.text.toString()
        }
    }

    private fun handleCurrenciesListResult(result: CurrenciesListResult) {
        when (result) {
            CurrenciesListResult.Loading -> binding.pbProgress.setVisible(true)
            is CurrenciesListResult.Success -> {
                adapter.setNewItemsStates(result.result)
                binding.pbProgress.setVisible(false)
            }
            is CurrenciesListResult.Error -> {
                showErrorDialog(result.errorData)
                binding.pbProgress.setVisible(false)
            }
        }
    }

    private fun handleFavoriteCurrencyResult(result: FavoriteCurrencyResult) {
        when (result) {
            FavoriteCurrencyResult.Default -> { /**doNothing**/ }
            is FavoriteCurrencyResult.Success -> adapter.changeItemState(result.newItemUiState, showOnlyFavorites)
            is FavoriteCurrencyResult.Error -> showErrorDialog(result.errorData)

        }
    }

    private fun showErrorDialog(errorDialogData: ErrorDialogData) {
        ErrorDialog(errorDialogData).show(childFragmentManager, ErrorDialog::class.java.simpleName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}