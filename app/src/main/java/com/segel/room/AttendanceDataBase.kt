package com.segel.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.segel.model.Attendance
import com.segel.model.AttendanceApiResponse
import com.segel.model.Leaves
import com.segel.model.Profile


@Database(entities = [Profile::class, Attendance::class,Leaves::class], version =5)
abstract class AttendanceDataBase:RoomDatabase() {
    abstract fun  profileDao(): ProfileDAO
    abstract fun  attendanceDAO(): AttendanceDAO
    abstract fun  leavesDAO():LeavesDAO

    companion object {
        private var INSTANCE: AttendanceDataBase? = null

        fun getDatabase(context: Context): AttendanceDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AttendanceDataBase::class.java,
                    "SegelSoftDb"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}