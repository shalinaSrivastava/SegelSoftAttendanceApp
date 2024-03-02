package com.segel.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.segel.R

class HolidayCalenderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday_calender)
        val  backImg =findViewById<ImageView>(R.id.backImg)
        backImg.setOnClickListener{view->clickBack()}
    }

    private fun clickBack() {
        val mIntent = Intent(this, DashboardActivity::class.java)
        startActivity(mIntent)
        finish()
    }
}