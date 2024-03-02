package com.segel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val punchIn: String,
    val punchOut: String,
    val serverId: Int,
    val synced: Int)
