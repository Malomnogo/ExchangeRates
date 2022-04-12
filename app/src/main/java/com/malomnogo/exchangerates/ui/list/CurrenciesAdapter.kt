package com.malomnogo.exchangerates.ui.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malomnogo.exchangerates.R
import com.malomnogo.exchangerates.databinding.ItemCurrencyBinding

class CurrenciesAdapter(
    private var states: MutableList<CurrencyItemUiState> = mutableListOf()
) : RecyclerView.Adapter<CurrenciesAdapter.CurrenciesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesViewHolder =
        CurrenciesViewHolder(
            ItemCurrencyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        holder.bind(states[position])
    }

    override fun onBindViewHolder(
        holder: CurrenciesViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (payload in payloads) {
                if (payload == FAVOURITE_CURRENCY_PAYLOAD) {
                    holder.setFavorite(states[position])
                }
            }
        }
    }

    override fun getItemCount(): Int = states.size

    class CurrenciesViewHolder(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CurrencyItemUiState) {
            with(binding) {
                tvName.text = item.symbol
                tvValue.text = item.rate.toString()
                setFavorite(item)
                ivFavorite.setOnClickListener {
                    item.onFavoriteClick(item.symbol, !item.isFavorite)
                    item.isFavorite = !item.isFavorite
                    setFavorite(item)
                }
            }
        }

        fun setFavorite(currencyState: CurrencyItemUiState) {
            val favoriteIcon = if (currencyState.isFavorite) R.drawable.ic_baseline_star_24
            else R.drawable.ic_baseline_star_outline_24
            binding.ivFavorite.setImageResource(favoriteIcon)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNewItemsStates(itemsStates: List<CurrencyItemUiState>) {
        this.states.clear()
        this.states.addAll(itemsStates)
        notifyDataSetChanged()
    }

    fun changeItemState(newItemUiState: CurrencyItemUiState, removeFavourite: Boolean = false) {
        val index = states.indexOfFirst { it.symbol == newItemUiState.symbol }
        if (index == -1) return

        if (removeFavourite && !newItemUiState.isFavorite) {
            states.removeAt(index)
            notifyItemRemoved(index)
            return
        }

        states[index] = newItemUiState
        notifyItemChanged(index, FAVOURITE_CURRENCY_PAYLOAD)
    }

    companion object {
        const val FAVOURITE_CURRENCY_PAYLOAD = "FavoriteCurrencyPayload"
    }

}

