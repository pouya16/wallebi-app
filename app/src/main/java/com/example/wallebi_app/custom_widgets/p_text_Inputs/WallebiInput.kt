package com.example.wallebi_app.custom_widgets.p_text_Inputs

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.Layout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.wallebi_app.R


class WallebiInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int=0
) : LinearLayout(context,attrs,defStyle,defStyleRes){
    init {
        LayoutInflater.from(context).inflate(R.layout.p_layout_input,this,true)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,R.styleable.wallebi_input,0,0)
            var enableIcon = resources.getBoolean(typedArray.getResourceId(R.styleable.wallebi_input_enableIcon,0))


        }
    }
}

/*
class WallebiInput : LinearLayout {


    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    )
            : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    )
            : super(context, attrs, defStyleAttr, defStyleRes)
}
*/

