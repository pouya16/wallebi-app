package com.example.wallebi_app.api

import android.app.Activity
import com.example.wallebi_app.R
import com.example.wallebi_app.helpers.StringHelper
import org.json.JSONObject

class ResponseErrorHandler(val context:Activity){




    fun createError(code: Int = 400, message: JSONObject,header:String) {

        val errorBody = try {
            when(code){
                400 -> context.getString(R.string.network_error)
                404 -> (message.getString(context.getString(R.string.error_response_key)) +
                        "\n" + context.getString(R.string.remain_dotted) +
                        context.getString(R.string.remain_response_key))
                429 -> (message.getString(context.getString(R.string.error_response_key)) + "\n" +
                        context.getString(R.string.available_in) + " " +
                        message.getString(context.getString(R.string.availableIn)) +
                        message.getString(context.getString(R.string.available_response_key)))
                else -> context.getString(R.string.network_error)
            }
        }catch (e:Exception){
            context.getString(R.string.failed_json)
        }

        context.runOnUiThread {
            StringHelper.showSnackBar(context, errorBody, header, 1)
        }
    }
}