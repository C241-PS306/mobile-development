package com.dicoding.wearshare.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

class RegisterButton : AppCompatButton {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private var txtColor: Int = 0

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = if (isEnabled) "Signup" else "Invalid data!"
    }
}
