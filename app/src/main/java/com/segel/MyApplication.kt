package com.segel

import android.app.Application
import com.segel.repository.MainRepository
import com.segel.repository.RetrofitService
import com.segel.room.AttendanceDataBase

class MyApplication : Application(){
        lateinit var mainReposetry: MainRepository
    override fun onCreate() {
        super.onCreate()
        val retrofitService = RetrofitService.getInstance()
        val dataBase =  AttendanceDataBase.getDatabase(applicationContext)
        mainReposetry=MainRepository(retrofitService,dataBase,applicationContext)
    }
    }