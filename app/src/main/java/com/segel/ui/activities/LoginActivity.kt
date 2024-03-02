package com.segel.ui.activities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.segel.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.segel.databinding.ActivityLoginBinding
import com.segel.repository.MainRepository
import com.segel.repository.RetrofitService
import com.segel.room.AttendanceDataBase
import com.segel.viewModel.activities.LoginScreenViewModel
import com.segel.viewModel.factory.HomeViewModelFactory

class LoginActivity : AppCompatActivity() {
    lateinit var viewModel: LoginScreenViewModel
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val retrofitService = RetrofitService.getInstance()
        val dataBase = AttendanceDataBase.getDatabase(this)
        val mainRepository = MainRepository(retrofitService,dataBase,this@LoginActivity)
        viewModel = ViewModelProvider(this,HomeViewModelFactory(mainRepository,this@LoginActivity)).get(LoginScreenViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        validation()
    }
    private fun validation() {
        if (binding.emailTv.text.toString().isEmpty()){
            binding.emailTv.error="Required Field!"
        }

    }
}
