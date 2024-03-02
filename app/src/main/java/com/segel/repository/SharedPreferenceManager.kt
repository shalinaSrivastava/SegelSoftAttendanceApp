package com.segel.repository
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreferenceManager(private val context: Context) {

    private val LOGGED_IN_KEY = "LOGGED_IN"
    private val Employee_ID_KEY = "EMPLOYEE_ID"


    private val preferences: SharedPreferences = context.getSharedPreferences("segelIndia", Context.MODE_PRIVATE)

    var loggedIn: Boolean
        get() = preferences.getBoolean(LOGGED_IN_KEY, false)
        set(value) = preferences.edit().putBoolean(LOGGED_IN_KEY, value).apply()


    var Employee_ID: Int
        get() = preferences.getInt(Employee_ID_KEY, 0)
        set(value) = preferences.edit().putInt(Employee_ID_KEY,value).apply()
}



