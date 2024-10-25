package com.example.union

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AttendanceHistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var attendanceAdapter: AttendanceAdapter
    private val attendanceList = mutableListOf<Attendance>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_attendance_history, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewAttendance)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        attendanceAdapter = AttendanceAdapter(attendanceList)
        recyclerView.adapter = attendanceAdapter
        loadAttendanceData()
        return view
    }

    private fun loadAttendanceData() {
        db.collection("attendance")
            .get()
            .addOnSuccessListener { result ->
                attendanceList.clear()
                for (document in result) {
                    val attendance = document.toObject<Attendance>()
                    attendanceList.add(attendance)
                }
                attendanceAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }
}
