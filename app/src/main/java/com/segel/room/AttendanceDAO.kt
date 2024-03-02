package com.segel.room

import androidx.room.*
import com.segel.model.Attendance
import com.segel.model.AttendanceApiResponse

@Dao
interface AttendanceDAO {
       @Query("SELECT * FROM attendance")
    fun getAll(): List<Attendance>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(attendance: Attendance)
        @Query("UPDATE attendance SET punchOut = :newPunchOutValue, serverId = :newServerIdValue, synced = :newSyncedValue WHERE id = :newId")
        fun update(newId: Int, newPunchOutValue: String, newServerIdValue: Int, newSyncedValue: Int)
    }



