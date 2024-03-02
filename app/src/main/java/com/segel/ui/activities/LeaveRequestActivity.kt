package com.segel.ui.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModelProvider
import com.segel.R
import com.segel.databinding.ActivityLeaveRequestBinding
import com.segel.repository.MainRepository
import com.segel.repository.RetrofitService
import com.segel.room.AttendanceDataBase
import com.segel.viewModel.activities.LeavesRequestViewModel
import com.segel.viewModel.factory.HomeViewModelFactory
import java.util.*

class LeaveRequestActivity : AppCompatActivity() {

    lateinit var viewModel: LeavesRequestViewModel
    private lateinit var binding: ActivityLeaveRequestBinding
    lateinit var startDateTv:TextView
     lateinit var endDateTv:TextView
     lateinit var applyLeave:TextView
     lateinit var selectedOption:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_leave_request)
        startDateTv=findViewById(R.id.startDateTv)
        endDateTv=findViewById(R.id.endDateTv)
        applyLeave=findViewById(R.id.applyLeave)
        val retrofitService = RetrofitService.getInstance()
        val dataBase = AttendanceDataBase.getDatabase(this)
        val mainRepository = MainRepository(retrofitService,dataBase,this)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(mainRepository,this@LeaveRequestActivity)).get(LeavesRequestViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val radioGroup = findViewById<RadioGroup>(R.id.typeOfDay)
        viewModel.selectedOption.observe(this, { selectedOption ->
        })
        val defaultOption = "1"
        viewModel.setSelectedOption(defaultOption)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
             selectedOption = selectedRadioButton.text.toString()
            viewModel.setSelectedOption(selectedOption)
        }
        applyLeave.setOnClickListener {
             setValidation()
        }
        startDateTv.setOnClickListener(){
            openDatePicker(viewModel.startDate)
        }

        endDateTv.setOnClickListener(){
            openDatePicker(viewModel.endDate)
        }
    }
    private fun setValidation() {
        val leaveType = viewModel.leaveTypeTextView.get()
        val startDate = viewModel.startDate.get()
        val endDate = viewModel.endDate.get()
        val leaveReason = viewModel.leaveReason.value

        if (leaveType.isNullOrEmpty()) {
            setToast("Leave type must be selected")
        } else if (startDate.isNullOrEmpty() || endDate.isNullOrEmpty()) {
            setToast("Start Date and End Date must be selected")
        } else if (leaveReason.isNullOrEmpty()) {
            setToast("Required Reason!")
        } else {
            leaveRequest()
        }
    }

    private fun setToast(mesg: String) {
        Toast.makeText(applicationContext, mesg, Toast.LENGTH_SHORT).show()
    }
    private fun leaveRequest() {
        viewModel.submittedRequestLeave()
        viewModel.leavesRequest.observe(this) { response ->
            var message: String=""
            response?.let { apiResponse ->
                if (apiResponse.status.equals("success")) {
                    message = "Your request for leave is submitted successfully!"
                } else {
                    message = "Sorry!!...there is some issue\n Your request for leave is not submitted!"
                }
                viewModel.apiResponseDialog(message)
            }
        }
    }
    private fun openDatePicker(date: ObservableField<String>) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                viewModel.setDate(selectedYear, selectedMonth, selectedDayOfMonth)
                val selectedDateStr = String.format("%04d-%02d-%02d",selectedYear,selectedMonth + 1,selectedDayOfMonth)
                date.set(selectedDateStr)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()

    }
}





