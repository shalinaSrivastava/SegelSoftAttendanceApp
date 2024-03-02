package com.segel.viewModel.fragment

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.segel.model.Attendance
import com.segel.model.AttendanceApiResponse
import com.segel.model.Leaves
import com.segel.model.Profile
import com.segel.repository.MainRepository
import com.segel.repository.SharedPreferenceManager
import com.segel.ui.activities.HolidayCalenderActivity
import com.segel.ui.activities.LeaveActivity
import com.segel.ui.activities.LoginActivity
import kotlinx.coroutines.*

class HomeFragmentViewModel(private val mainRepository: MainRepository,private val context: Context) : ViewModel() {

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    }
    val profileLiveData: LiveData<Profile> = mainRepository.profile
    val attendanceLiveData: LiveData<AttendanceApiResponse> = mainRepository.attendance
      fun logOut() {

          val sharedPreferenceManager = SharedPreferenceManager(context)
          sharedPreferenceManager.loggedIn = false
          val mIntent = Intent(context, LoginActivity::class.java)
          mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          context.startActivity(mIntent)

    }
    fun getProfile() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            mainRepository.getProfile()
        }
    }
    fun leaves() {
        val mIntent = Intent(context, LeaveActivity::class.java)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(mIntent)
    }
    fun holidayList() {
        val mIntent = Intent(context, HolidayCalenderActivity::class.java)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(mIntent)
    }
    fun postAttendance(jsonData: JsonArray, dataBase_Array: ArrayList<Int>) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            mainRepository.attendance(jsonData,dataBase_Array)

        }
    }
}



