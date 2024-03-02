package com.segel.viewModel.activities

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.segel.repository.MainRepository
import com.segel.repository.SharedPreferenceManager
import com.segel.ui.activities.LoginActivity
import com.segel.ui.activities.Registration
import kotlinx.coroutines.*

class LoginScreenViewModel (private val mainRepository: MainRepository,private val context:Context) : ViewModel() {
    val email = ObservableField<String>()
    val password = ObservableField<String>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    }
    val usernamePattern = "^[a-zA-Z0-9._-]+\$".toRegex()
    val passwordMinLength = 8
    val isUsernameValid = MutableLiveData<Boolean>()
    val isPasswordValid = MutableLiveData<Boolean>()
    fun validateUsername(username: String) {
        isUsernameValid.value = username.matches(usernamePattern)
    }
    fun validatePassword(password: String) {
        isPasswordValid.value = password.length >= passwordMinLength
    }
    fun validation() {
             email.set("s@gmail.com")
             password.set("123")
             val emailPattern = "[a-zA-Z0-9._-]+@gmail\\.com"
            if (email.get().toString().isEmpty()){
                email.set("Input required")
            } else if (!email.get().toString().matches(emailPattern.toRegex())) {
                email.set("Invalid email address")
            }else if (password.get().toString().isEmpty()) {
                password.set("Input required")
            } else {
                login()
            }
        }
    fun login() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val jsonLogin = JsonObject().apply {
                addProperty("email",email.get().toString())
                addProperty("password",password.get().toString())
            }
            mainRepository.logIn(jsonLogin)
        }
    }
    fun registrationClick()
    {
        val mIntent = Intent(context, Registration::class.java)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(mIntent)
    }
}

