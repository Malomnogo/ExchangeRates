package com.malomnogo.exchangerates.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.malomnogo.exchangerates.R
import com.malomnogo.exchangerates.databinding.DialogErrorBinding

class ErrorDialog(private val errorDialogData: ErrorDialogData) : DialogFragment() {

    private var _binding: DialogErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        _binding = DialogErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvErrorTitle.text = errorDialogData.message
            btnRetry.setOnClickListener {
                errorDialogData.retryAction()
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}

data class ErrorDialogData(
    val message: String? = null,
    val retryAction: () -> Unit = {},
    @StringRes val retryButtonNameRes: Int? = R.string.retry
)