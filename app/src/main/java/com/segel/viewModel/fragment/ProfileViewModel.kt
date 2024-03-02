package com.segel.viewModel.fragment

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.segel.model.Attendance
import com.segel.model.Profile
import com.segel.repository.MainRepository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*


class ProfileViewModel(private val mainRepository: MainRepository) : ViewModel() {
    val imagePath = ObservableField<String>()

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    }
   val profileLiveData: LiveData<Profile> = mainRepository.profile
    fun getProfile() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            mainRepository.getProfile()
            profileLiveData.value

            val base64Image=profileLiveData.value?.image
            imagePath.set(base64Image)
            Log.d("base64Image","1."+base64Image)
        }
    }
    }
