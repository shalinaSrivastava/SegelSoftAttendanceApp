package com.segel.viewModel.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.segel.R
import com.segel.model.ApiResponse
import com.segel.repository.MainRepository
import com.segel.ui.activities.LeaveActivity
import kotlinx.coroutines.*
import android.widget.TextView

class LeavesRequestViewModel (private val mainRepository: MainRepository, private val context: Context) : ViewModel()  {
    val leaveTypeTextView = ObservableField<String>()
    val startDate = ObservableField<String>()
    val endDate = ObservableField<String>()
   val leaveReason = MutableLiveData<String>()

    var job: Job? = null
    val leavesRequest: LiveData<ApiResponse> = mainRepository.leavesRequest
    private val _selectedOption = MutableLiveData<String>()
    val selectedOption: LiveData<String> get() = _selectedOption

    fun setSelectedOption(option: String) {
        _selectedOption.value = option
    }
    val selectedDate = MutableLiveData<String>()
    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
        selectedDate.value = formattedDate
    }
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

    }
    fun submittedRequestLeave() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val type:Int

            if (leaveTypeTextView.get().toString().equals("Sick Leave")){
                type=  1
            }else  if (leaveTypeTextView.get().toString().equals("Casual Leave")){
                type=2
            }else   if (leaveTypeTextView.get().toString().equals("Others Leaves")){
                type=3
            }else{
                type= 4
            }
            val jsonData = JsonObject().apply {
                addProperty("type", type)
                addProperty("duration_type", 1)
                addProperty("start_date", startDate.get())
                addProperty("end_date", endDate.get().toString())
                addProperty("reason", leaveReason.value.toString())
            }
            mainRepository.leavesRequest(jsonData)
        }
    }
    fun onClickBack(){
        val mIntent = Intent(context, LeaveActivity::class.java)
        context.startActivity(mIntent)
    }
    fun leaveType(): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_list, null, false)
        val customeListView = view.findViewById<ListView>(R.id.customeListView)
        val leaveList = arrayOf("Casual Leave", "Sick Leave", "Others Leaves")

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setView(view)

        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, leaveList)
        customeListView.adapter = adapter

        customeListView.setOnItemClickListener { parent, _, position, _ ->
            val designationName = adapter.getItem(position) as String
            leaveTypeTextView.set(designationName)
            dialog.dismiss()

        }
        dialog.show()
        return dialog
    }
    fun apiResponseDialog(message: String): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_response, null, false)

        val messageTv = view.findViewById<TextView>(R.id.customeMessage)
        val custome_submitted_tv = view.findViewById<TextView>(R.id.custome_submitted_tv)
        messageTv.text = message
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setView(view)
        custome_submitted_tv.setOnClickListener {
            leaveTypeTextView.set("")
            startDate.set("")
            endDate.set("")
            leaveReason.value=""
            dialog.dismiss()
        }
        dialog.show()
        return dialog
    }




}