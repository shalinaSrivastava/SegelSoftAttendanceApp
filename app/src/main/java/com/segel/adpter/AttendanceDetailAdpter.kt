package com.segel.adpter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.segel.R
import com.segel.model.Attendance
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
class AttendanceDetailAdpter : ListAdapter<Attendance, AttendanceDetailAdpter.ViewHolder>(AttendanceDetail()) {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.adpter_attendance_detail, parent, false)
            return ViewHolder(itemView)
        }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val date: TextView = itemView.findViewById(R.id.date_attendance)
        private val punchIn_time: TextView = itemView.findViewById(R.id.punchIn_attendance)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: Attendance) {

            val locationTimeAsString = item.punchIn
            val locationTime = locationTimeAsString.toLong()
            if (locationTime != null) {

                val instant = Instant.ofEpochMilli(locationTime)
                val zoneId = ZoneId.systemDefault()
                val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
                val dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
                date.text = dateFormatter.format(zonedDateTime)
                val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
                punchIn_time.text = timeFormatter.format(zonedDateTime)

            }else{
                date.text = "-"
                punchIn_time.text = "-"
            }
        }
    }
        @RequiresApi(Build.VERSION_CODES.N)
        private class AttendanceDetail : DiffUtil.ItemCallback<Attendance>() {
            override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
                return oldItem == newItem

            }
        }
    
}

