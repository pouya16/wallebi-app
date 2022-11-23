package com.example.wallebi_app.helpers

import android.app.Activity
import android.os.CountDownTimer
import android.widget.Button
import com.example.wallebi_app.R

class UiHelper(val activity: Activity,val btn: Button){

    fun countDown() = activity.runOnUiThread{
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                btn.text = "" + seconds / 60 + ":" + seconds % 60
            }

            override fun onFinish() {
                btn.setText(R.string.get_code)
                btn.isActivated = true
            }
        }.start()
    }



}