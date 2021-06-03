package sn.momzo.penguinpayapplication.utils

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import com.orhanobut.logger.Logger
import java.lang.StringBuilder

class PhoneTextFormatter(private var mEditText: EditText, private var mPattern: String) :
    TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val phone = StringBuilder(s)


        if (count > 0 && !isValid(phone.toString())) {
            for (i in phone.indices) {
                val c = mPattern[i]
                if (c != '#' && c != phone[i]) {
                    phone.insert(i, c)
                }
            }
            mEditText.setText(phone)
            mEditText.setSelection(mEditText.text.length)
        }
    }

    override fun afterTextChanged(s: Editable) {}
    private fun isValid(phone: String): Boolean {
        for (i in phone.indices) {
            val c = mPattern[i]
            if (c == '#') continue
            if (c != phone[i]) {
                return false
            }
        }
        return true
    }

    init {
        val maxLength = mPattern.length
        mEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        Logger.d("mPattern $mPattern}")
    }
}