package com.malomnogo.exchangerates.ui.dialog.symbol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.malomnogo.exchangerates.databinding.DialogSymbolsBinding
import com.malomnogo.exchangerates.ui.list.CurrenciesListResult
import com.malomnogo.exchangerates.ui.list.ListViewModel
import com.malomnogo.exchangerates.utils.ErrorDialog
import com.malomnogo.exchangerates.utils.ErrorDialogData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SymbolsDialogFragment(private val onClick: (String) -> Unit) : DialogFragment() {

    private var _binding: DialogSymbolsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<ListViewModel>()
    private lateinit var adapter: SymbolsDialogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = DialogSymbolsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeData()
    }

    private fun initUi() {
        adapter = SymbolsDialogAdapter {
            onClick.invoke(it)
            dismiss()
        }
        with(binding) {
            currenciesList.adapter = adapter
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currenciesListResult.collect(::handleCurrenciesListResult)
            }
        }
    }

    private fun handleCurrenciesListResult(result: CurrenciesListResult) {
        when (result) {
            CurrenciesListResult.Loading -> { /**doNothing**/ }
            is CurrenciesListResult.Success -> adapter.submitList(result.result)
            is CurrenciesListResult.Error -> showErrorDialog(result.errorData)
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