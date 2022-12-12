package com.example.wallebi_app.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.wallebi_app.R
import com.example.wallebi_app.acitivities.LoginRegisterActivity
import com.example.wallebi_app.api.GetMeApi
import com.example.wallebi_app.api.HttpCallback
import com.example.wallebi_app.api.data.NetworkModel
import com.example.wallebi_app.database.LoginData
import com.example.wallebi_app.database.UserLogin
import com.example.wallebi_app.database.models.MeModel
import com.example.wallebi_app.helpers.StringHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import okhttp3.Response
import org.json.JSONObject

class HomeFragment : Fragment() {
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/
    lateinit var txtLoginStatus:TextView
    lateinit var txtEmailAddress:TextView
    lateinit var progressBar: ProgressBar
    lateinit var btnRegister:MaterialButton
    lateinit var btnLogin:MaterialButton
    var isLogin = false
    var meClass : MeModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        btnRegister = view.findViewById<MaterialButton>(R.id.btn_by)
        btnLogin = view.findViewById<MaterialButton>(R.id.btn_sell)
        txtLoginStatus = view.findViewById(R.id.txt_login_status)
        txtEmailAddress = view.findViewById(R.id.txt_login_email)
        progressBar = view.findViewById(R.id.progressbar)
        progressBar.visibility = View.GONE
        var userLogin = UserLogin(requireContext())
        var userModel = userLogin.getUser()
        if (userModel!= null){
            //txtEmailAddress.visibility = View.VISIBLE
            //txtEmailAddress.text = userModel.email
        }

        view.findViewById<MaterialCardView>(R.id.btn_bankcards).setOnClickListener {
            if(LoginData.access_token.length > 3){
                findNavController(it).navigate(R.id.action_homeFragment_to_bankFragment)
            }else{
                val intent = Intent(context,LoginRegisterActivity::class.java)
                intent.putExtra("mode",0)
                context?.startActivity(intent)
            }
        }


        view.findViewById<MaterialCardView>(R.id.btn_setting).setOnClickListener {
            if(isLogin){
                findNavController(it).navigate(R.id.action_homeFragment_to_settingFragment)
            }else{
                val intent = Intent(context,LoginRegisterActivity::class.java)
                intent.putExtra("mode",0)
                context?.startActivity(intent)
            }
        }

        btnLogin.setOnClickListener{
            val intent = Intent(context,LoginRegisterActivity::class.java)
            intent.putExtra("mode",0)
            context?.startActivity(intent)

        }
        btnRegister.setOnClickListener{

            val intent = Intent(context,LoginRegisterActivity::class.java)
            intent.putExtra("mode",1)
            context?.startActivity(intent)
        }


        return view
    }

    override fun onResume() {
        Log.i("Log1","resume called")
        super.onResume()
        if(LoginData.access_token!=null && LoginData.access_token.length > 4){
            btnLogin.visibility = View.GONE
            btnRegister.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            txtLoginStatus.text = "You are Login"
            isLogin = true
            if(LoginData.meClass == null){
                Log.i("Log1","going to get me.")
                getMe()
            }
        }


    }

    private fun getMe(){
        val callback: HttpCallback = object : HttpCallback {

            var mainHandler: Handler = Handler(context!!.getMainLooper())
            override fun onFialure(response: Response, throwable: Throwable) {
                try {
                } catch (e: Exception) {
                }
                mainHandler.post { progressBar.visibility = View.GONE
                    StringHelper.showSnackBar(context as Activity,"failed to get data","Network",2)}
            }

            override fun onSuccess(response: Response) {
                Log.i("Log1", "" + response.code)
                try {
                    val res = response.body!!.string()
                    Log.i("Log1: ", "response: $res")
                    val jsonObject = JSONObject(res)
                    if(response.code == 200){
                        if(jsonObject.getBoolean("success")){
                            NetworkModel("Choose Network","Choose Network",0.0,"Choose Network")
                            val gson = Gson()
                            meClass = gson.fromJson(
                                jsonObject.getJSONObject("msg").toString(),
                                MeModel::class.java
                            )
                        }
                    }

                } catch (e: Exception) {
                    Log.i("Log1", "failed to convert to json: $e")
                }
                mainHandler.post { progressBar.visibility = View.GONE
                if(meClass!= null){
                    txtEmailAddress.visibility = View.VISIBLE
                    txtEmailAddress.text = meClass!!.full_email
                }
                }
            }
        }
        GetMeApi(context,callback)
    }

}