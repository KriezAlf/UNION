package com.example.union

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var editTextUsername: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textView : TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        db = Firebase.firestore
        editTextUsername = findViewById(R.id.username)
        editTextPassword = findViewById(R.id.password)
        buttonLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.registerNow)

        buttonLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                        return@addOnSuccessListener
                    }

                    val email = documents.documents[0].getString("email")
                    if (email != null) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->
                                progressBar.visibility = View.GONE
                                if (task.isSuccessful) {
                                    Log.d(TAG, "signInWithEmail:success")
                                    val user = auth.currentUser
                                    updateUI(user, username)
                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                                    Toast.makeText(baseContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                                    updateUI(null, "")
                                }
                            }
                    } else {
                        Toast.makeText(this, "User data incomplete", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                    Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
        }
        textView.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateUI(user: FirebaseUser?, username: String) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("USERNAME", username)
            }
            startActivity(intent)
            finish()
        } else {
            // Login failed, display an error message or take appropriate action
        }
    }
}