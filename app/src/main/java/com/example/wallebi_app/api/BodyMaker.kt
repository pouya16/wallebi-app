package com.example.wallebi_app.api

import android.util.Log
import com.example.wallebi_app.helpers.StringHelper

class BodyMaker {

    companion object {

        @JvmOverloads
        fun getBody(list: List<BodyHandlingModel>? = null): String? {
            list?.let {
                var body = "{\n"
                for (index in list.indices) {
                    with(list[index]) {
                        body = if (index != list.lastIndex)
                            when (type) {
                                "string" -> "$body    \"${key}\": \"${value}\",\n"
                                else -> {
                                    "$body    \"${key}\": ${value},\n"
                                }
                            }
                        else{
                            when (type) {
                                "string" -> "$body    \"${key}\": \"${value}\"\n"
                                else -> {
                                    "$body    \"${key}\": ${value}\n"
                                }
                            }
                        }
                    }
                }
                body = "$body}"
                return body
            }
            return null
        }

    }

}