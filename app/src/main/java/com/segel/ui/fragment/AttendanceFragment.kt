package com.segel.ui.fragment

import android.content.ContentProvider
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.segel.R
import com.segel.adpter.AttendanceDetailAdpter
import com.segel.databinding.FragmentAttendanceBinding
import com.segel.model.Attendance
import com.segel.room.AttendanceDataBase
import com.segel.viewModel.fragment.AttendanceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AttendanceFragment : Fragment() {

    lateinit var  viewModel : AttendanceViewModel
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var attendanceDetail_rv: RecyclerView
    private lateinit var adapter: AttendanceDetailAdpter
    lateinit var appContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_attendance, container, false)
        appContext = requireContext().applicationContext

        binding.lifecycleOwner = this

        attendanceDetail_rv = binding.attendanceDetailRv.findViewById(R.id.attendanceDetail_rv)
        attendanceDetail_rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = AttendanceDetailAdpter()
        attendanceDetail_rv.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            val dataBase = AttendanceDataBase.getDatabase(requireContext())
            val allData = dataBase.attendanceDAO().getAll()
            adapter.submitList(allData)
        }
        return binding.root
    }
}
