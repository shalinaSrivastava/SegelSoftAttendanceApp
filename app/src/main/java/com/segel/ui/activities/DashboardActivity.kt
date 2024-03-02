package com.segel.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.segel.R
import com.segel.databinding.ActivityDashboardBinding
import com.segel.ui.fragment.AttendanceFragment
import com.segel.ui.fragment.HomeFragment
import com.segel.ui.fragment.ProfileFragment
import com.segel.viewModel.activities.DashboardViewModel

class DashboardActivity : AppCompatActivity() {
    private lateinit var home: ImageView
    private lateinit var history: ImageView
    private lateinit var prfile: ImageView

    private var isAlternateImage = false
    private lateinit  var home_lay:RelativeLayout
    private lateinit  var prfile_lay:RelativeLayout
    private lateinit  var history_lay:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        val viewModel = DashboardViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val fragmentManager = supportFragmentManager
         prfile = findViewById(R.id.prfile)
         home = findViewById(R.id.home)
        history=findViewById(R.id.history)
        home_lay=findViewById(R.id.home_lay)
        prfile_lay=findViewById(R.id.prfile_lay)
        history_lay = findViewById(R.id.history_lay)

        val fragment = HomeFragment()
        fragmentManager.replaceFragment(fragment)

        prfile.setOnClickListener{
            setBackgroundProfile()
            val fragment = ProfileFragment()
            fragmentManager.replaceFragment(fragment)
        }
        home.setOnClickListener{
            setBackgroundHome()
            val fragment = HomeFragment()
            fragmentManager.replaceFragment(fragment)
        }
        history.setOnClickListener{
            setBackgroundHistory()
            val fragment = AttendanceFragment()
            fragmentManager.replaceFragment(fragment)
        }
    }
    private fun setBackgroundHome() {
        home.setImageResource(R.drawable._home_black)
        home_lay.setBackgroundResource(R.drawable.stoke_red)
        prfile.setImageResource(R.drawable.ic_baseline_profile_24)
        history.setImageResource(R.drawable.ic_baseline_calendar_today_24)
        prfile_lay.setBackgroundResource(0)
        history_lay.setBackgroundResource(0)
    }
    private fun setBackgroundHistory() {
        home.setImageResource(R.drawable._home)
        home_lay.setBackgroundResource(0)
        prfile.setImageResource(R.drawable.ic_baseline_profile_24)
        history.setImageResource(R.drawable._calendar_black)
        prfile_lay.setBackgroundResource(0)
        history_lay.setBackgroundResource(R.drawable.stoke_red)
    }
    private fun setBackgroundProfile() {
        home.setImageResource(R.drawable._home)
        home_lay.setBackgroundResource(0)
        prfile.setImageResource(R.drawable._profile_black)
        history.setImageResource(R.drawable.ic_baseline_calendar_today_24)
        history_lay.setBackgroundResource(0)
        prfile_lay.setBackgroundResource(R.drawable.stoke_red)
    }
    fun FragmentManager.replaceFragment(fragment: Fragment)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun toggleImage() {
        if (isAlternateImage) {
            home.setImageResource(R.drawable._home) // Change to the initial image
        } else {
            home.setImageResource(R.drawable._home_alt) // Change to the alternate image
        }
        isAlternateImage = !isAlternateImage
    }
}