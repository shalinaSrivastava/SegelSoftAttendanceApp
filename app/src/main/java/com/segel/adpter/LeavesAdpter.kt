package com.segel.adpter
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.segel.R
import com.segel.model.Leaves
import com.segel.ui.activities.LeaveActivity
class LeavesAdpter( context: Context) : ListAdapter<Leaves, LeavesAdpter.ViewHolder>(LeaveList()) {
    private  val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.adpter_leaves, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val reason_leave: TextView = itemView.findViewById(R.id.reason_leave)
        private val type_leave: TextView = itemView.findViewById(R.id.type_leave)
        private val date_leave: TextView = itemView.findViewById(R.id.date_leave)
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: Leaves) {
           val leaveType:String
            if (item.type==1){
                leaveType="Sick Leave"
                type_leave.setTextColor(Color.YELLOW)
            }else  if (item.type==2){
                leaveType="Casual leave"
                type_leave.setTextColor(Color.GREEN)
            }else  if (item.type==3){
                leaveType="Others Leaves"
            }else  if (item.type==4){
                leaveType="leave without informing"
                type_leave.setTextColor(Color.RED)
            }
            else{
                leaveType="leave without informing"
                type_leave.setTextColor(Color.RED)
            }
            reason_leave.text = item.reason
            type_leave.text = leaveType
            date_leave.text = item.start_date
        }
        }
    }
    private class LeaveList : DiffUtil.ItemCallback<Leaves>() {
        override fun areItemsTheSame(oldItem: Leaves, newItem: Leaves): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Leaves, newItem: Leaves): Boolean {
            return oldItem == newItem
        }
    }
