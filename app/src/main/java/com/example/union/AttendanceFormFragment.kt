package com.example.union

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AttendanceFormFragment : Fragment() {

    private lateinit var buttonTakePhoto: Button
    private lateinit var buttonSubmit: Button
    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    private var submissionCount = 0
    private var currentDate = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_attendance_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.imageView)
        buttonTakePhoto = view.findViewById(R.id.buttonTakePhoto)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)

        // Get the current date (to track submissions)
        currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        buttonTakePhoto.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        buttonSubmit.setOnClickListener {
            submitAttendance()
        }
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            val imageFile = createImageFile()
            imageUri = FileProvider.getUriForFile(requireContext(), "com.example.union.fileProvider", imageFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun createImageFile(): File {
        val storageDir = requireContext().filesDir
        return File.createTempFile("attendance_photo_${System.currentTimeMillis()}", ".jpg", storageDir)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            Toast.makeText(requireContext(), "Photo taken successfully!", Toast.LENGTH_SHORT).show()
            imageView.setImageURI(imageUri) // Display the image
            imageView.visibility = View.VISIBLE // Show the ImageView
        }
    }

    private fun submitAttendance() {
        db.collection("attendance")
            .whereEqualTo("date", currentDate)
            .get()
            .addOnSuccessListener { documents ->
                submissionCount = documents.size()

                if (submissionCount == 0) {
                    saveAttendance("Absen Masuk")
                } else if (submissionCount == 1) {
                    saveAttendance("Absen Keluar")
                } else {
                    Toast.makeText(requireContext(), "Attendance already submitted twice today.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveAttendance(attendanceType: String) {
        val attendanceData = hashMapOf(
            "attendanceType" to attendanceType,
            "date" to currentDate,
            "timestamp" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
            "imageUri" to imageUri.toString()
        )

        db.collection("attendance").add(attendanceData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "$attendanceType submitted successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to submit attendance: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val REQUEST_IMAGE_CAPTURE = 1
    }
}