package com.segel.viewModel.activities

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.segel.ui.activities.Registration

class DashboardViewModel : ViewModel() {
    val email = ObservableField<String>()
    val password = ObservableField<String>()

    val responseLiveData = MutableLiveData<String>()

    suspend fun registraionOnClick(context: Context){
        val mIntent = Intent(context, Registration::class.java)
        context.startActivity(mIntent)
    }
    suspend fun userLogin(context: Context)
    {

    }
}