package com.example.wallebi_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.wallebi_app.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : BaseActivity() {

    private lateinit var currentNavController: LiveData<NavController>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragmentActivity = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2)
                as NavHostFragment
        val navController = navHostFragmentActivity.navController
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.menu.getItem(2).isEnabled = false


    }


    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}