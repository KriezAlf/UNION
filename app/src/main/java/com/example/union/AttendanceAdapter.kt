package com.example.union

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class Attendance(
    val attendanceType: String = "",
    val timestamp: String = "",
    val imageUri: String = ""
)

class AttendanceAdapter(private val attendanceList: List<Attendance>) :
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val attendanceType: TextView = itemView.findViewById(R.id.textViewAttendanceType)
        val timestamp: TextView = itemView.findViewById(R.id.textViewDate)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewAttendance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attendance_item, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendance = attendanceList[position]
        holder.attendanceType.text = attendance.attendanceType
        holder.timestamp.text = attendance.timestamp
        Glide.with(holder.itemView.context)
            .load(attendance.imageUri)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }
}
