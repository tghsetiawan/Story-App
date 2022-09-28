package com.teguh.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.teguh.storyapp.R

class CustomEditTextEmail: TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                error = null
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    error = context.getString(R.string.invalid_email)
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.hint_email)
    }
}