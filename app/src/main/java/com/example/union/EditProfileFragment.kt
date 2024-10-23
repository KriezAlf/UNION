package com.example.union

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileFragment : Fragment() {

    private lateinit var editTextName: EditText
    private lateinit var editTextNIM: EditText
    private lateinit var buttonSaveProfile: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextName = view.findViewById(R.id.editTextName)
        editTextNIM = view.findViewById(R.id.editTextNIM)
        buttonSaveProfile = view.findViewById(R.id.buttonSaveProfile)

        buttonSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val name = editTextName.text.toString()
        val nim = editTextNIM.text.toString()

        // Save data to Firebase Firestore
        val profileData = hashMapOf("name" to name, "nim" to nim)
        db.collection("users").document("user_id").set(profileData)
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }
}
