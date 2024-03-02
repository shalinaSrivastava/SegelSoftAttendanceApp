package com.segel.ui.fragment
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.segel.R
import com.segel.databinding.FragmentHomeBinding
import com.segel.repository.MainRepository
import com.segel.repository.RetrofitService
import com.segel.room.AttendanceDataBase
import com.segel.viewModel.factory.HomeViewModelFactory
import com.segel.viewModel.fragment.HomeFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.gms.location.LocationRequest
import com.segel.model.Attendance
import android.location.Geocoder
import com.google.android.gms.maps.GoogleMap
import java.io.IOException


class HomeFragment : Fragment() {
    lateinit var viewModel: HomeFragmentViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var punchIn: TextView
    lateinit var appContext:Context
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val dataArray: ArrayList<Attendance> = ArrayList()
    val pendingAttendance: ArrayList<Int> = ArrayList()
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var countdownTextView: TextView
    private lateinit var punchOut: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
         appContext = requireContext().applicationContext
        val retrofitService = RetrofitService.getInstance()
        val dataBase = AttendanceDataBase.getDatabase(appContext)
        val mainRepository = MainRepository(retrofitService,dataBase, appContext)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(mainRepository,appContext)).get(HomeFragmentViewModel::class.java)
        punchIn=binding.root.findViewById(R.id.punchIn)
        punchOut=binding.root.findViewById(R.id.punchOut)
        countdownTextView = binding.viewTimer.findViewById(R.id.view_timer)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        requestLocationPermission()

        //val repository =(myApplication as MyApplication).mainReposetry
       //viewModel = ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeFragmentViewModel::class.java)
     /*
     viewModel.profileLiveData.observe(viewLifecycleOwner, { profile ->
            if (profile != null) {
                name.set(profile.name)
                designation.set(profile.designation)
            }
        })
        */
        viewModel.getProfile()
        punchIn.setOnClickListener { view ->
            val destinationAddress = "Kh no. - 86/24, Hiran kudna Mor, Mundka Udyog Nagar, Mundka Industrial Area, Mundka, New Delhi, 110041"
            val destinationLocation = getLocationFromAddress(destinationAddress)

            if (destinationLocation != null) {
                getSingleUpdateLocation("punchIn",destinationLocation)
            }
        }
        punchOut.setOnClickListener { view ->
            val destinationAddress = "Kh no. - 86/24, Hiran kudna Mor, Mundka Udyog Nagar, Mundka Industrial Area, Mundka, New Delhi, 110041"
            val destinationLocation = getLocationFromAddress(destinationAddress)
            if (destinationLocation != null) {
                getSingleUpdateLocation("punchOut",destinationLocation)
            }
        }
           setCountdownTimer()
           return binding.root
    }
    private fun setCountdownTimer() {
        val totalTimeMillis: Long = 300000
        countdownTimer = object : CountDownTimer(totalTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                val timeLeft = String.format("%02d:%02d", minutes, seconds)
                countdownTextView.text = timeLeft
            }
            override fun onFinish() {
                countdownTextView.text = "00:00"
            }
        }
        countdownTimer.start()
    }
    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            val dataBase = AttendanceDataBase.getDatabase(appContext)
            val data = dataBase.attendanceDAO().getAll()
            dataArray.clear()
            pendingAttendance.clear()
            dataArray.addAll(data)
            for (item in dataArray) {
                if (item.synced == 0) {
                    pendingAttendance.add(item.id)
                    val jsonArray = JsonArray().apply {
                        val jsonData = JsonObject().apply {
                            addProperty("punch_in", item.punchIn)
                            addProperty("punch_out", "")
                            addProperty("server_id", 0)
                        }
                        add(jsonData)
                    }
                    viewModel.postAttendance(jsonArray,pendingAttendance)
                }
            }
        }
    }
    private fun punchIn(time: Long?) {

        val jsonArray = JsonArray().apply {
            val jsonData = JsonObject().apply {

                addProperty("punch_in", time)
                addProperty("punch_out", "")
                addProperty("server_id", 0)
            }
            add(jsonData)
        }
        viewModel.postAttendance(jsonArray, pendingAttendance)
        var message: String=""
        viewModel.attendanceLiveData?.let { apiResponse ->
            if (apiResponse.value?.status.equals("success")) {
                message = "Attendance submitted successfully!"
            } else {
                message = "Sorry!!...there is some issue\n Your Attendance  is not submitted!"
            }
Toast.makeText(appContext,message,Toast.LENGTH_SHORT).show()
        }
    }
    private fun punchOut(time: Long?) {

        CoroutineScope(Dispatchers.IO).launch {
            val dataBase = AttendanceDataBase.getDatabase(appContext)

            val data = dataBase.attendanceDAO().getAll()
            Log.d("punchOut","data= "+data)

            dataArray.clear()
            pendingAttendance.clear()
            dataArray.addAll(data)
            for (item in dataArray) {
                Log.d("punchOut","synced= "+item.synced)
                Log.d("punchOut","punchOut= "+item.punchOut)

                if (item.synced == 1&& item.punchOut.equals("")) {
                    pendingAttendance.add(item.id)

                    val jsonArray = JsonArray().apply {
                        val jsonData = JsonObject().apply {
                            addProperty("punch_in", item.punchIn)
                            addProperty("punch_out", time)
                            addProperty("server_id", item.serverId)
                        }
                        add(jsonData)
                    }
                    viewModel.postAttendance(jsonArray,pendingAttendance)
                    var message: String=""
                    viewModel.attendanceLiveData?.let { apiResponse ->
                            if (apiResponse.value?.status.equals("success")) {
                                message = "Attendance submitted successfully!"
                        } else {
                            message = "Sorry!!...there is some issue\n Your Attendance  is not submitted!"
                        }
                        Toast.makeText(appContext,message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    }
    private fun getSingleUpdateLocation(attendanceType: String, destinationLocation: Location) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (checkLocationPermission()) {
            val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    val accuracy = location?.accuracy ?: Float.MAX_VALUE

                    if (accuracy <= 500) {
                        val distanceToDestination = location?.distanceTo(destinationLocation)

                        if (distanceToDestination != null) {
                            if (attendanceType == "punchIn" && distanceToDestination <= 500) {
                                punchIn(location?.time)
                            } else if (attendanceType == "punchOut" && distanceToDestination <= 500) {
                                punchOut(location?.time)
                            } else {
                                Toast.makeText(appContext,"You are currently outside the office premises or do not meet the eligibility criteria for Punching In.",Toast.LENGTH_SHORT)                        }
                        }
                    }

                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } else {
            requestLocationPermission()
        }
    }
    fun getLocationFromAddress(address: String): Location? {
        val geocoder = Geocoder(appContext)
        try {
            val results = geocoder.getFromLocationName(address, 1)
            if (results!!.isNotEmpty()) {
                val location = results[0]
                val latitude = location.latitude
                val longitude = location.longitude
                val locationObj = Location("Destination")
                locationObj.latitude = latitude
                locationObj.longitude = longitude
                return locationObj
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}