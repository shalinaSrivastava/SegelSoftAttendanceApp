package com.segel.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.segel.R
import com.segel.adpter.LeavesAdpter
import com.segel.databinding.ActivityLeaveBinding
import com.segel.repository.MainRepository
import com.segel.repository.RetrofitService
import com.segel.room.AttendanceDataBase
import com.segel.viewModel.activities.LeavesViewModel
import com.segel.viewModel.factory.HomeViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeaveActivity : AppCompatActivity() {

    lateinit var viewModel: LeavesViewModel
    private lateinit var binding: ActivityLeaveBinding
    private lateinit var adapter: LeavesAdpter
      private lateinit var  requestLeaveTv:ImageView
      private lateinit var request_frameLayout:FrameLayout
      private lateinit var leaves_rv:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding=DataBindingUtil.setContentView(this, R.layout.activity_leave)
        val retrofitService = RetrofitService.getInstance()
        val dataBase = AttendanceDataBase.getDatabase(this)
        val mainRepository = MainRepository(retrofitService,dataBase,this)
        viewModel = ViewModelProvider(this,HomeViewModelFactory(mainRepository,this@LeaveActivity)
        ).get(LeavesViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
         requestLeaveTv = findViewById<ImageView>(R.id.requestLeaveTv)
        request_frameLayout=findViewById(R.id.request_frameLayout)
        request_frameLayout.visibility = View.GONE
        viewModel.leaves()
        binding.lifecycleOwner = this
        leaves_rv = binding.leavesRv.findViewById(R.id.leaves_rv)
        leaves_rv.layoutManager = LinearLayoutManager(this@LeaveActivity)

        adapter = LeavesAdpter(this@LeaveActivity)
        leaves_rv.adapter = adapter
        CoroutineScope(Dispatchers.IO).launch {
            val dataBase = AttendanceDataBase.getDatabase(this@LeaveActivity)
            val allData = dataBase.leavesDAO().getAll()
            adapter.submitList(allData)
        }
    }

}