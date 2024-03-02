package com.segel.viewModel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.segel.repository.MainRepository
import com.segel.ui.fragment.ProfileFragment
import com.segel.viewModel.fragment.HomeFragmentViewModel
import com.segel.viewModel.fragment.ProfileViewModel
import kotlin.reflect.KClass

class ProfileViewModelFactory(
    private val repository: MainRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfileFragment::class.java)) {
            ProfileViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}