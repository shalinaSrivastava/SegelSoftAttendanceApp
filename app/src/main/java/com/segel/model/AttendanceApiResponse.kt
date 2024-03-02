package com.segel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class AttendanceApiResponse (val status: String, val server_ids_data: Array<Int>)

