package com.example.wallebi_app.fragments

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import com.example.wallebi_app.R
import com.example.wallebi_app.acitivities.LoginRegisterActivity
import com.example.wallebi_app.database.LoginData
import com.example.wallebi_app.database.UserLogin
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class HomeFragment : Fragment() {
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/
    lateinit var txtLoginStatus:TextView
    lateinit var txtEmailAddress:TextView
    var isLogin = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnRegister = view.findViewById<MaterialButton>(R.id.btn_by)
        val btnLogin = view.findViewById<MaterialButton>(R.id.btn_sell)
        txtLoginStatus = view.findViewById(R.id.txt_login_status)
        txtEmailAddress = view.findViewById(R.id.txt_login_email)
        var userLogin = UserLogin(requireContext())
        var userModel = userLogin.getUser()
        if (userModel!= null){
            txtEmailAddress.visibility = View.VISIBLE
            txtEmailAddress.text = userModel.email
        }


        view.findViewById<MaterialCardView>(R.id.btn_setting).setOnClickListener {
            if(isLogin){
                Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_settingFragment)
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
            txtLoginStatus.text = "You are Login"
            isLogin = true
        }

    }

}