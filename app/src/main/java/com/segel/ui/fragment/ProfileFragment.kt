package com.segel.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.segel.R
import com.segel.databinding.FragmentProfileBinding
import com.segel.repository.MainRepository
import com.segel.repository.RetrofitService
import com.segel.room.AttendanceDataBase
import com.segel.viewModel.factory.HomeViewModelFactory
import com.segel.viewModel.fragment.ProfileViewModel
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    lateinit var appContext: Context
    lateinit var changePic_iv:ImageView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        appContext = requireContext().applicationContext
        val retrofitService = RetrofitService.getInstance()
        val dataBase = AttendanceDataBase.getDatabase(appContext)
        val mainRepository = MainRepository(retrofitService, dataBase, appContext)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(mainRepository, appContext)).get(ProfileViewModel::class.java)
        binding.lifecycleOwner = this
        changePic_iv=binding.root.findViewById(R.id.changePic_iv)
        binding.viewModel = viewModel
        viewModel.getProfile()
           var imagePath=viewModel.profileLiveData.value?.image
        Picasso.get()
            .load(imagePath)
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(changePic_iv)
        return binding.root
    }
}