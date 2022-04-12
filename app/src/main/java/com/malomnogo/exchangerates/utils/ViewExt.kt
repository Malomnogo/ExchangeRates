package com.malomnogo.exchangerates.utils

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup

fun View.setVisible(isShow: Boolean) {
    if (isShow) this.visibility = View.VISIBLE else this.visibility = View.GONE
}

fun RadioGroup.getSelectedId(): Int {
    val radioButton: RadioButton = this.findViewById(this.checkedRadioButtonId)
    return this.indexOfChild(radioButton)
}

fun RadioGroup.selectById(id: Int) {
    val radioButtonId = this.getChildAt(id).id
    this.check(radioButtonId)
}