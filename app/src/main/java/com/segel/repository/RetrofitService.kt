package com.segel.repository

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.segel.apiRequest.LoginRequest
import com.segel.model.*
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitService {


        companion object {
            private const val BASE_URL = "http://192.168.88.11:2000/api/v1/"
            var retrofitService: RetrofitService? = null
            fun getInstance() : RetrofitService {
                if (retrofitService == null) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    retrofitService = retrofit.create(RetrofitService::class.java)
                }
                return retrofitService!!
            }

        }

    @POST("employees")
    suspend fun postRegistraion(@Body registraionJson: JsonObject): Response<ApiResponse>
    @POST("login")
    suspend fun login(@Body loginJson: JsonObject): Response<LoginApiResponse>

    @GET("employees/{employeeId}")
    suspend fun getProfile(@Path("employeeId") employeeId: Int): Response<ProfileApiResponse>

    @POST("punchinpunchout/{employeeId}")
    suspend fun attendance_punchIn_punchOut(@Path("employeeId") employeeId: Int,@Body jsonAttendance: JsonArray): Response<AttendanceApiResponse>

    @POST("leaves/{employeeId}")
    suspend fun leavesRequest(@Path("employeeId") employeeId: Int,@Body jsonAttendance: JsonObject): Response<ApiResponse>

    @GET("leaves/{employeeId}")
    suspend fun getLeaves(@Path("employeeId") employeeId: Int): Response<LeavesApiResponse>

}