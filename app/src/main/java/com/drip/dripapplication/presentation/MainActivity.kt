package com.drip.dripapplication.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
    }

    private fun isSignedIn(): Boolean {
        val sign = intent.getBooleanExtra("sign", false)
        return sign
    }
}
