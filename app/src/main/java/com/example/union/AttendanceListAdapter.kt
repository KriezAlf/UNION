package com.example.union

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AttendanceListAdapter(private val attendanceList: List<AttendanceRecord>) :
    RecyclerView.Adapter<AttendanceListAdapter.AttendanceViewHolder>() {

    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewAttendanceType: TextView = itemView.findViewById(R.id.textViewAttendanceType)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val textViewTimestamp: TextView = itemView.findViewById(R.id.textViewTimestamp)
        val imageViewAttendance: ImageView = itemView.findViewById(R.id.imageViewAttendance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attendance_record, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendance = attendanceList[position]
        holder.textViewAttendanceType.text = attendance.attendanceType
        holder.textViewDate.text = attendance.date
        holder.textViewTimestamp.text = attendance.timestamp
        Picasso.get().load(attendance.imageUri).into(holder.imageViewAttendance)
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }
}