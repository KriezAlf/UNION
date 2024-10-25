package com.example.union

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var usernameTextView: TextView
    private lateinit var nimTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var editProfileButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        usernameTextView = view.findViewById(R.id.textViewUsername)
        nimTextView = view.findViewById(R.id.textViewNim)
        emailTextView = view.findViewById(R.id.textViewEmail)
        logoutButton = view.findViewById(R.id.buttonLogout)
        editProfileButton = view.findViewById(R.id.buttonEditProfile)

        loadUserProfile()

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, Login::class.java)
            startActivity(intent)
            activity?.finish()
        }

        editProfileButton.setOnClickListener {
/*            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .addToBackStack(null)
                .commit()*/
        }

        return view
    }

    private fun loadUserProfile() {
        val user = auth.currentUser
        user?.let {
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username") ?: ""
                        val nim = document.getString("nim") ?: ""
                        val email = document.getString("email") ?: ""

                        usernameTextView.text = username
                        nimTextView.text = nim
                        emailTextView.text = email
                    } else {
                        Log.d("ProfileFragment", "No such document")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("ProfileFragment", "Error fetching user data", e)
                    Toast.makeText(context, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
