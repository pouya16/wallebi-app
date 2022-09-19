package com.example.wallebi_app.custom_widgets.p_text_Inputs

import android.annotation.TargetApi
import android.content.Context
import android.media.Image
import android.os.Build
import android.text.Layout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.wallebi_app.R
import com.google.android.material.button.MaterialButton


class WallebiInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int=0
) : LinearLayout(context,attrs,defStyle,defStyleRes){

    var enableBtn = false
    var flagCounterBtn = false
    var isCountDown = false

    lateinit var text: EditText
    lateinit var txtLabel: TextView
    lateinit var txtError: TextView
    lateinit var txtInfo: TextView
    lateinit var btn: MaterialButton
    lateinit var iconActionPlace: RelativeLayout
    lateinit var iconImage: ImageButton


    init {
        val layoutInflater = LayoutInflater.from(context).inflate(R.layout.p_layout_input,this,true)
        attrs?.let {
            text = layoutInflater.findViewById(R.id.txt_input)
            txtLabel = layoutInflater.findViewById(R.id.txt_label_p)
            txtError = layoutInflater.findViewById(R.id.txt_error_p)
            txtInfo = layoutInflater.findViewById(R.id.txt_info_p)
            btn = layoutInflater.findViewById(R.id.btn_action_input)
            iconActionPlace = layoutInflater.findViewById(R.id.icon_action_place_p)
            iconImage = layoutInflater.findViewById(R.id.image_wallebi_p)
            val typedArray = context.obtainStyledAttributes(it,R.styleable.wallebi_input,0,0)
            var enableIcon = resources.getBoolean(typedArray.getResourceId(R.styleable.wallebi_input_enableIcon,0))
            var iconPlace = layoutInflater.findViewById<RelativeLayout>(R.id.wallebi_country_icon)
            if(enableIcon) {iconPlace.visibility = View.VISIBLE} else iconPlace.visibility = View.GONE
            var txt = resources.getString(typedArray.getResourceId(R.styleable.wallebi_input_textViewText,0),null)
            if(txt!=null) layoutInflater.findViewById<TextView>(R.id.txt_prefix).setText(txt) else
                layoutInflater.findViewById<TextView>(R.id.txt_prefix).visibility = View.GONE
            var hint = resources.getString(typedArray.getResourceId(R.styleable.wallebi_input_labelText,0),null)
            if(hint!=null) {
                txtLabel.visibility = View.VISIBLE
                txtLabel.text = hint
            }else {txtLabel.visibility = View.GONE}

            var description = resources.getString(typedArray.getResourceId(R.styleable.wallebi_input_description,0),null)
            if(description!=null) {
                txtInfo.visibility = View.VISIBLE
                txtInfo.text = hint
            }else {txtInfo.visibility = View.GONE}
            enableBtn= resources.getBoolean(typedArray.getResourceId(R.styleable.wallebi_input_enableButton,0))
            if(enableBtn){
                btn.visibility = View.VISIBLE
                btn.text = resources.getString(typedArray.getResourceId(R.styleable.wallebi_input_buttonText,0))
            } else btn.visibility = View.GONE

            var error = resources.getString(typedArray.getResourceId(R.styleable.wallebi_input_errorText,0),null)
            if(error != null){
                txtError.text = error
                txtError.visibility = View.VISIBLE
            }else txtError.visibility = View.GONE

            var enableCountDown = resources.getBoolean(typedArray.getResourceId(R.styleable.wallebi_input_enableCounter,0))
            if(enableCountDown){
                isCountDown = true
            }

            var counterToBtn = resources.getBoolean(typedArray.getResourceId(R.styleable.wallebi_input_counterToBtn,0))
            if(counterToBtn) {
                flagCounterBtn = true
            }

        }
    }

    open fun changeCounterToBtn(){

    }

    open fun getText() : String {
        return text.text.toString()
    }

    open fun setLalel(label: String) {txtLabel.text = label}






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

