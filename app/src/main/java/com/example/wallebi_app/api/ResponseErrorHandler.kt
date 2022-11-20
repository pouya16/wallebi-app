package com.example.wallebi_app.api

import android.content.Context
import android.os.Looper
import android.util.Log
import com.example.wallebi_app.base.MyApplication
import com.example.wallebi_app.helpers.StringHelper
import org.json.JSONObject
import java.util.logging.Handler

class ResponseErrorHandler(val context:Context){




    fun createError(code: Int = 400, message: JSONObject) {
            Log.i("Log1","we got into error handler");
            android.os.Handler(context.mainLooper).post {
                StringHelper.showSnackBar(context,"error code is 404","network error",1)
            }
        }
}