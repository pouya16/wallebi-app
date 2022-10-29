package com.example.wallebi_app.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wallebi_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequests {

    Context context;
    String main_url;
    JSONObject jsonObject;

    public VolleyRequests(Context context){
        this.context = context;
        main_url = context.getString(R.string.base_url);
    }





}
