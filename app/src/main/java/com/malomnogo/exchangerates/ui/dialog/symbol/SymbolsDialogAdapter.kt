package com.malomnogo.exchangerates.ui.dialog.symbol

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malomnogo.exchangerates.databinding.ItemSymbolBinding
import com.malomnogo.exchangerates.ui.list.CurrencyItemUiState

class SymbolsDialogAdapter(private val onItemClickListener: (String) -> Unit) :
    ListAdapter<CurrencyItemUiState, SymbolsDialogAdapter.CurrenciesDialogViewHolder>(
        DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesDialogViewHolder =
        CurrenciesDialogViewHolder(
            ItemSymbolBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )

    override fun onBindViewHolder(holder: CurrenciesDialogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CurrenciesDialogViewHolder(private val binding: ItemSymbolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(state: CurrencyItemUiState) {
            with(binding.root) {
               text = state.symbol
               setOnClickListener {
                    onItemClickListener(state.symbol)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CurrencyItemUiState>() {
            override fun areItemsTheSame(
                oldList: CurrencyItemUiState,
                newList: CurrencyItemUiState,
            ) = oldList.symbol == newList.symbol

            override fun areContentsTheSame(
                oldList: CurrencyItemUiState,
                newList: CurrencyItemUiState,
            ) = oldList == newList
        }
    }
}

