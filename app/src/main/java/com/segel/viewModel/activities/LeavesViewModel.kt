package com.segel.viewModel.activities
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.segel.model.Leaves
import com.segel.model.Profile
import com.segel.repository.MainRepository
import com.segel.ui.activities.DashboardActivity
import com.segel.ui.activities.LeaveRequestActivity
import com.segel.ui.activities.LoginActivity
import kotlinx.coroutines.*

class LeavesViewModel(private val mainRepository: MainRepository,private val context: Context) : ViewModel() {
    val name = ObservableField<String>()
    val leavesLiveData: LiveData<Leaves> = mainRepository.leaves

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    }
    fun onClickBack(){
        val mIntent = Intent(context, DashboardActivity::class.java)
        context.startActivity(mIntent)
    }
    fun onLeaveRequest(){
        val mIntent = Intent(context, LeaveRequestActivity::class.java)
        context.startActivity(mIntent)
    }
    fun leaves() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            mainRepository.getLeaves()
        }
    }
}