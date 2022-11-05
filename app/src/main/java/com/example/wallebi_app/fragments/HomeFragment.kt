package com.example.wallebi_app.fragments

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.wallebi_app.R
import com.example.wallebi_app.acitivities.LoginRegisterActivity
import com.example.wallebi_app.database.UserLogin
import com.google.android.material.button.MaterialButton

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
        super.onResume()

    }

}