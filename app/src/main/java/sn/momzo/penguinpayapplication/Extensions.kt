package sn.momzo.penguinpayapplication

import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showError() {
    this.error = context.getString(R.string.field_mandatory)
    this.requestFocus()
}


fun TextInputLayout.removeErrorWhenTextChanged() {
    this.editText?.addTextChangedListener {
        if (it?.isNotBlank() == true) {
            this.removeError()
        }
    }
}

fun TextInputLayout.removeError() {
    this.error = null
}