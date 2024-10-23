package com.example.union

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var textViewDay: TextView
    private lateinit var textViewDate: TextView
    private lateinit var textViewTime: TextView
    private lateinit var imageTakeAttendance: ImageView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        textViewDay = view.findViewById(R.id.textViewDay)
        textViewDate = view.findViewById(R.id.textViewDate)
        textViewTime = view.findViewById(R.id.textViewTime)
        imageTakeAttendance = view.findViewById(R.id.imageTakeAttendance)
        updateDateTime()
        imageTakeAttendance.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AttendanceFormFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun updateDateTime() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        val currentTime = timeFormat.format(calendar.time)
        textViewDay.text = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        textViewDate.text = currentDate
        textViewTime.text = currentTime

        handler.postDelayed({ updateDateTime() }, 1000)
    }
}
