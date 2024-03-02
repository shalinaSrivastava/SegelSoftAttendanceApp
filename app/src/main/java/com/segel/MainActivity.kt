package com.segel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.segel.repository.SharedPreferenceManager
import com.segel.ui.activities.DashboardActivity
import com.segel.ui.activities.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
            val sharedPreferenceManager = SharedPreferenceManager(this)

            val isLoggedIn = sharedPreferenceManager.loggedIn

            if (isLoggedIn) {
                openNewActivity(DashboardActivity::class.java)
            } else {
                openNewActivity(LoginActivity::class.java)
            }
        }, 2000)
    }
    fun openNewActivity(activityClass: Class<*>) {
        val mIntent = Intent(this@MainActivity,activityClass)
        startActivity(mIntent)
        finish()
    }

}