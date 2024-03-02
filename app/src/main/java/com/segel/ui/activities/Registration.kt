package com.segel.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mikhaellopez.circularimageview.CircularImageView
import com.segel.R
import com.segel.databinding.ActivityRegistrationBinding
import com.segel.repository.MainRepository
import com.segel.repository.RetrofitService
import com.segel.room.AttendanceDataBase
import com.segel.util.ConvertImageToBase64Task
import com.segel.viewModel.RegistraionViewModel
import com.segel.viewModel.factory.HomeViewModelFactory
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class Registration : AppCompatActivity() {
    lateinit var viewModel: RegistraionViewModel
    private val PICK_IMAGE_REQUEST = 1
   private lateinit var profilePic: CircularImageView
   private lateinit var edit_profilePic:ImageView
   private lateinit var registrationTv:TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegistrationBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        val retrofitService = RetrofitService.getInstance()
        val dataBase = AttendanceDataBase.getDatabase(this)

        val repository = MainRepository(retrofitService,dataBase,this)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(repository,this@Registration)).get(RegistraionViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        profilePic = findViewById<CircularImageView>(R.id.profilePic)
        edit_profilePic = findViewById<ImageView>(R.id.edit_profilePic)
        registrationTv=findViewById(R.id.registrationTv)
        edit_profilePic.setOnClickListener { view->openGallery() }
        registrationTv.setOnClickListener{view->setValidation()}

    }
    private fun setValidation() {

        val name = viewModel.name.get()
        val contactNumber = viewModel.contactNumber.get()
        val designation = viewModel.designation.get()
        val confirmPassword = viewModel.confirmPassword.get()
        val password = viewModel.password.get()
        val eMail = viewModel.eMail.get()

        val emailPattern = "[a-zA-Z0-9._-]+@gmail\\.com"
        val emailPatternSegelIndia = "[a-zA-Z0-9._-]+@segelIndia\\.com"


        if (name.isNullOrEmpty()) {
        } else if (contactNumber.isNullOrEmpty()) {
            setToast("Name Required !")
        } else if (designation.isNullOrEmpty()) {
            setToast("Designation type must be selected")
        } else if (password.isNullOrEmpty()) {
            setToast("password Required !")
        }else if (confirmPassword.isNullOrEmpty()) {
            setToast("confirm Password Required !")
        }else if (!(password.equals(confirmPassword))) {
            setToast("password and confirm Password must be same")
        }  else if (!(eMail?.matches(emailPattern.toRegex()))!!||!(eMail?.matches(emailPatternSegelIndia.toRegex()))!!) {
            setToast("Invalid email address")
        }
        else {
            registraionResponse()
        }
    }
    private fun setToast(mesg: String) {
        Toast.makeText(applicationContext, mesg, Toast.LENGTH_SHORT).show()
    }
    private fun registraionResponse() {
        viewModel.registraion
        viewModel.registraion.observe(this) { response ->
            var message: String=""
            response?.let { apiResponse ->
                if (apiResponse.status.equals("success")) {
                    message = "Registraion successfully!"
                } else {
                    message = "Sorry!!...there is some issue\n Registraion is not submitted!"
                }
                viewModel.apiResponseDialog(message)
            }
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*" // Filter to show only images
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            selectedImageUri?.let {
                val inputStream = contentResolver.openInputStream(selectedImageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                if (bitmap != null) {
                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                    val compressedImageBytes = outputStream.toByteArray()
                    val base64Image = Base64.encodeToString(compressedImageBytes, Base64.DEFAULT)
                    val imageByteArray = Base64.decode(base64Image, Base64.DEFAULT)
                    val filePath = File.createTempFile("segel_profile_image", ".jpg", cacheDir)
                    val filePath_outputStream = FileOutputStream(filePath)
                    filePath_outputStream.write(imageByteArray)
                    filePath_outputStream.close()
                    Picasso.get()
                        .load(filePath)
                        .into(profilePic)
                    viewModel.imagePath.set(base64Image)
                }
            }
        }

    }
}
