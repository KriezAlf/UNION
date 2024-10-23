package com.example.union

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AttendanceHistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var attendanceListAdapter: AttendanceListAdapter
    private val attendanceList = mutableListOf<AttendanceRecord>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_attendance_history, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewAttendanceHistory)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        attendanceListAdapter = AttendanceListAdapter(attendanceList)
        recyclerView.adapter = attendanceListAdapter

        loadAttendanceRecords()

        return view
    }

    private fun loadAttendanceRecords() {
        db.collection("attendance")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                attendanceList.clear()
                for (document in documents) {
                    val record = AttendanceRecord(
                        attendanceType = document.getString("attendanceType") ?: "",
                        date = document.getString("date") ?: "",
                        timestamp = document.getString("timestamp") ?: "",
                        imageUri = document.getString("imageUri") ?: ""
                    )
                    attendanceList.add(record)
                }
                attendanceListAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Handle the error here, e.g., show a Toast message
                Log.e("AttendanceHistoryFragment", "Error getting documents: ", e)
            }

    }
}
