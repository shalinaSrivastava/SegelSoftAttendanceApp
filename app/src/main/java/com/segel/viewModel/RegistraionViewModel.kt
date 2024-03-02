package com.segel.viewModel
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.segel.R
import com.segel.model.ApiResponse
import com.segel.repository.MainRepository
import com.segel.ui.activities.LoginActivity
import kotlinx.coroutines.*

class RegistraionViewModel(private val repository: MainRepository,private val context: Context) : ViewModel() {
    val name = ObservableField<String>()
    val contactNumber = ObservableField<String>()
    val designation = ObservableField<String>()
    val eMail = ObservableField<String>()
    val confirmPassword = ObservableField<String>()
    val password = ObservableField<String>()
    val imagePath = ObservableField<String>()
    val registraion: LiveData<ApiResponse> = repository.registration


    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    }

     fun backClick(){
        val mIntent = Intent(context, LoginActivity::class.java)
        context.startActivity(mIntent)
    }
    fun onClickRegistration() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val jsonLogin = JsonObject().apply {
                addProperty("name",name.get().toString())
                addProperty("designation",designation.get().toString())
                addProperty("number",contactNumber.get().toString())
                addProperty("email",eMail.get().toString())
                addProperty("password",password.get().toString())
                addProperty("image",imagePath.get().toString())
            }
            repository.registrationEmployees(jsonLogin)
        }
    }
    fun designationList(): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_list, null, false)
        val designationLv = view.findViewById<ListView>(R.id.customeListView)
        val designationList = arrayOf("Software Engineer", "Sales", "Chartered Accountant", "Human Resources", "Others")
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setView(view)

        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, designationList)
        designationLv.adapter = adapter

        designationLv.setOnItemClickListener { parent, _, position, _ ->
            val designationName = adapter.getItem(position) as String
            designation.set(designationName)
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
            dialog.dismiss()
        }
        dialog.show()
        return dialog
    }

}