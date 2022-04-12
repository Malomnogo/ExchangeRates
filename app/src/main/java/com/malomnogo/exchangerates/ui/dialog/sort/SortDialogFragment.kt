package com.malomnogo.exchangerates.ui.dialog.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.malomnogo.exchangerates.databinding.DialogSortBinding
import com.malomnogo.exchangerates.utils.getSelectedId
import com.malomnogo.exchangerates.utils.selectById
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SortDialogFragment : DialogFragment() {

    private var _binding: DialogSortBinding? = null
    private val binding get() = _binding!!
    private val sortViewModel by activityViewModels<SortViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            sortViewModel.sortResult.collect { result ->
                binding.tvTitle.text = result.sortType.toString()
                binding.rgWay.selectById(result.sortType.ordinal)
                binding.rgType.selectById(if (result.isAscending) 0 else 1)
            }
        }

        binding.btnSave.setOnClickListener {
            val sort: SortType = SortType.values()[binding.rgWay.getSelectedId()]
            val sortType: Boolean = binding.rgType.getSelectedId() == 0
            sortViewModel.changeSortWay(sort, sortType)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}