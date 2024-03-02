package com.segel.repository

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.segel.model.*
import com.segel.room.AttendanceDataBase
import com.segel.ui.activities.DashboardActivity
import com.segel.ui.activities.LoginActivity
import com.segel.ui.activities.Registration
import com.segel.util.MyUtil
class MainRepository(private val retrofitService: RetrofitService, private val dataBase: AttendanceDataBase,private val applicationContext: Context) {
      val sharedPreferenceManager = SharedPreferenceManager(applicationContext)
        //registraion
        private val registraionLiveData = MutableLiveData<ApiResponse>()
    val registration: LiveData<ApiResponse>
        get() = registraionLiveData
        suspend fun registrationEmployees(json: JsonObject) {
            if (MyUtil.isInternetAvailable(applicationContext)) {
                val result = retrofitService.postRegistraion(json)
                Log.d("registraion","1."+result.body().toString())
                Log.d("registraion","2."+result.message())


                if (result.body() != null) {
                    if (result.body()?.status.equals("success")) {
                        Toast.makeText(applicationContext, "Registered" + result.body()?.status, Toast.LENGTH_SHORT).show()
                        val mIntent = Intent(applicationContext, LoginActivity::class.java)
                        applicationContext.startActivity(mIntent)

                    } else {
                        Toast.makeText(applicationContext, "Error:-" + result.body(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
        //Login
        suspend fun logIn(jsonLogin: JsonObject) {
            if (MyUtil.isInternetAvailable(applicationContext)) {

                val result = retrofitService.login(jsonLogin)
                if (result.body() != null) {
                    if (result.body()?.status.equals("success")) {
                        sharedPreferenceManager.loggedIn = true
                        sharedPreferenceManager.Employee_ID=result.body()!!.employee_id
                        val mIntent = Intent(applicationContext, DashboardActivity::class.java)
                        applicationContext.startActivity(mIntent)
                    } else if (result.body()?.status.equals("failed")) {
                        Toast.makeText(applicationContext, "there is some issue-" + result.body()!!.status, Toast.LENGTH_SHORT).show()
                        Log.d("profile","0.if."+result.body())
                    }
                    else {
                        Toast.makeText(applicationContext, "there is some issue-" + result.body()!!.status, Toast.LENGTH_SHORT).show()
                    }
                }
            }else {
                Toast.makeText(applicationContext, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
        //getProfile
        private val profileLiveData = MutableLiveData<Profile>()
        val profile: LiveData<Profile>
            get() = profileLiveData

        suspend fun getProfile() {
    if (MyUtil.isInternetAvailable(applicationContext)) {
        try {
            val response = retrofitService.getProfile(sharedPreferenceManager.Employee_ID)
            if (response.isSuccessful) {
                dataBase.profileDao().insert(response.body()!!.profile_data)
                profileLiveData.postValue(response.body()?.profile_data)
            } else {
                Toast.makeText(applicationContext,response.body()!!.status,Toast.LENGTH_SHORT).show()

            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext,"Error :- "+e,Toast.LENGTH_SHORT).show()
        }
    } else {
        val profile = dataBase.profileDao().getAll()
        profileLiveData.postValue(profile.get(0))
    }
}
       //Attendance
        private val attendanceLiveData = MutableLiveData<AttendanceApiResponse>()
    val attendance: MutableLiveData<AttendanceApiResponse>
            get() = attendanceLiveData
        suspend fun attendance(jsonData: JsonArray, IdOfflineArray: ArrayList<Int>) {
            var punchInValue: String? = null
            var punchOutValue: String? = null
            for (jsonElement in jsonData) {
                if (jsonElement is JsonObject) {
                    punchInValue = jsonElement.get("punch_in")?.asString
                    punchOutValue = jsonElement.get("punch_out")?.asString

                }
            }
            if (MyUtil.isInternetAvailable(applicationContext)) {
                val result = retrofitService.attendance_punchIn_punchOut(sharedPreferenceManager.Employee_ID,jsonData)
                if (result.body() != null) {
                    Log.d("Attendance","1."+result.body())
                    val responseData = result.body()

                    attendance.postValue(responseData)

                    if (result.body()?.status.equals("success")) {
                    /*    val serverIDS = result.body()?.server_ids_data
                        val uniqueServerIDS = ArrayList<Int>()
                        if (serverIDS != null) {
                            uniqueServerIDS.addAll(serverIDS)
                            for (sId in uniqueServerIDS) {
                                for (idOffline in IdOfflineArray)
                                 {
                                    dataBase.attendanceDAO().update(idOffline, punchOutValue.toString(), uniqueServerIDS.indexOf(sId), 1)
                               }
                            }
                        } else {*/
                            val attendance = Attendance(0, punchInValue.toString(), punchOutValue.toString(), result.body()?.server_ids_data!!.get(0), 1)
                            dataBase.attendanceDAO().insert(attendance)
                      //  }
                    }
                    }
            } else {
                val attendance = Attendance(0, punchInValue.toString(),punchOutValue.toString(), 0, 0)
                dataBase.attendanceDAO().insert(attendance)
                Log.d("Attendance","1."+dataBase.attendanceDAO().getAll())

            }
        }
//Leaves
private val leavesLiveData = MutableLiveData<Leaves>()
    val leaves: LiveData<Leaves>
        get() = leavesLiveData
    suspend fun getLeaves() {

        if (MyUtil.isInternetAvailable(applicationContext)) {
            try {

                val response = retrofitService.getLeaves(sharedPreferenceManager.Employee_ID)
                if (response.isSuccessful) {
                    dataBase.leavesDAO().insert(response.body()!!.leaves_data)
                } else {
                    Toast.makeText(applicationContext, "there is some issue-" + response.body()!!.status, Toast.LENGTH_SHORT).show()

                }
            } catch (e: Exception) {
                Toast.makeText(applicationContext,"Error :- "+e,Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext, "No Internet Connection", Toast.LENGTH_SHORT).show()

        }
    }
    private val leavesRequestLiveData = MutableLiveData<ApiResponse>()
    val leavesRequest: LiveData<ApiResponse>
        get() = leavesRequestLiveData
    suspend fun leavesRequest(jsonData: JsonObject) {
        if (MyUtil.isInternetAvailable(applicationContext)) {
            try {

                val response = retrofitService.leavesRequest(sharedPreferenceManager.Employee_ID,jsonData)
                if (response.isSuccessful) {
                    val responseData = response.body()
                    leavesRequestLiveData.postValue(responseData)
                } else {
                    Toast.makeText(applicationContext,"Error :- "+response.message(),Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(applicationContext,"Error :- "+e,Toast.LENGTH_SHORT).show()
            }
        } else {

        }
    }
}



