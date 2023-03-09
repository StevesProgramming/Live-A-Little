package com.example.live_a_little

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var txtSignup: TextView

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var dbRef: DatabaseReference

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsername = findViewById(R.id.usernameInput)
        etPassword = findViewById(R.id.passwordInput)
        dbRef = FirebaseDatabase.getInstance("https://project-cw-34e62-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users")

        btnLogin= findViewById(R.id.btnLogout)
        txtSignup = findViewById(R.id.txtSignup)

        btnLogin.setOnClickListener{
            login()
        }

        txtSignup.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    private fun login(){
        firebaseAuth = FirebaseAuth.getInstance()

        val email = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (email.isEmpty()) {
            etUsername.error = "Please enter a username"
        } else if (password.isEmpty()) {
            etPassword.error = "Please enter a password"
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

