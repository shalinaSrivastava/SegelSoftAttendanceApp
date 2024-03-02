package com.segel.viewModel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.segel.repository.MainRepository
import com.segel.viewModel.RegistraionViewModel
import com.segel.viewModel.activities.LeavesRequestViewModel
import com.segel.viewModel.activities.LeavesViewModel
import com.segel.viewModel.activities.LoginScreenViewModel
import com.segel.viewModel.fragment.HomeFragmentViewModel
import com.segel.viewModel.fragment.ProfileViewModel

class HomeViewModelFactory(private val repository: MainRepository, private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)) {
            return HomeFragmentViewModel(repository,context) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(RegistraionViewModel::class.java)){
            return  RegistraionViewModel(repository,context) as T
        }else if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)){
            return  LoginScreenViewModel(repository,context) as T
        }else if (modelClass.isAssignableFrom(LeavesViewModel::class.java)){
            return  LeavesViewModel(repository,context) as T
        }else if(modelClass.isAssignableFrom(LeavesRequestViewModel::class.java)){
           return LeavesRequestViewModel(repository,context) as T
        }
        throw IllegalArgumentException("ViewModel Not Found")
    }

}