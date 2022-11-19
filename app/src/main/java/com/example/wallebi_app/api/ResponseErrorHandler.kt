package com.example.wallebi_app.api

import android.content.Context
import com.example.wallebi_app.base.MyApplication

class ResponseErrorHandler(var context: Context) {
    fun createError(code:Int = 200,message:String = ""){
        if(context == MyApplication.getAppContext()){
            
        }
    }
}