package com.example.union

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextUsername: TextInputEditText
    private lateinit var editTextNIM: TextInputEditText
    private lateinit var buttonReg: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textView : TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        db = Firebase.firestore
        auth = Firebase.auth
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        editTextUsername = findViewById(R.id.username)
        editTextNIM = findViewById(R.id.NIM)
        buttonReg = findViewById(R.id.btn_Register)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.loginNow)

        buttonReg.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val username = editTextUsername.text.toString()
            val nim = editTextNIM.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (username.isEmpty()) {
                Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (nim.isEmpty()) {
                Toast.makeText(this, "Enter nim", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userData = hashMapOf(
                            "username" to username,
                            "nim" to nim,
                            "email" to email,
                            "password" to password
                        )
                        db.collection("users").document(user!!.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                Log.d(TAG, "Account created!")
                                progressBar.visibility = View.GONE
                                updateUI(user, nim, username)
                            }
                            .addOnFailureListener {
                                    e ->
                                Log.w(TAG, "Failed to create account", e)
                                progressBar.visibility = View.GONE
                                Toast.makeText(this, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
        }
        textView.setOnClickListener {
            val intent = Intent(this, Login::class.java) // Replace LoginActivity with your actual login activity
            startActivity(intent)
        }
    }

    private fun updateUI(user: FirebaseUser?, username: String, nim: String) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("USERNAME", username)
                putExtra("NIM", nim)
            }
            startActivity(intent)
            finish()
        } else {
            // Registration failed, display an error message or take appropriate action
        }
    }
}