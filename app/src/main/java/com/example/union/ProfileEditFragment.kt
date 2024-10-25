/*
package com.example.union

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileFragment : Fragment() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var buttonSaveProfile: Button
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextUsername = view.findViewById(R.id.editTextUsername)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPhone = view.findViewById(R.id.editTextPhone)
        buttonSaveProfile = view.findViewById(R.id.buttonSaveProfile)

        loadUserProfile()

        buttonSaveProfile.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun loadUserProfile() {
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        editTextUsername.setText(document.getString("username"))
                        editTextEmail.setText(document.getString("email"))
                        editTextPhone.setText(document.getString("phone"))
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to load profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveUserProfile() {
        val username = editTextUsername.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()

        if (userId != null) {
            val userProfile = hashMapOf(
                "username" to username,
                "email" to email,
                "phone" to phone
            )

            db.collection("users").document(userId)
                .set(userProfile)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    returnToHomeFragment()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun returnToHomeFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, HomeFragment())
            .commit()
    }
}
*/
