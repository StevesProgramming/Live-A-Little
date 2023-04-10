package com.example.live_a_little

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var txtSignup: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmail = findViewById(R.id.emailInput)
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

        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (email.isEmpty()) {
            etEmail.error = "Please enter an email"
        } else if (password.isEmpty()) {
            etPassword.error = "Please enter a password"
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
                    checkAdmin(email)
                }
                else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun checkAdmin(email: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        val admins = db.collection("admins")

        admins
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { adminsDocuments ->
                if (adminsDocuments.isSuccessful) {
                    val admin = adminsDocuments.result

                    if (admin.isEmpty) {
                        openHome()
                    } else {
                        openAdmin()
                    }
                }

            }
    }

    private fun openHome(){
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    private fun openAdmin() {
        val intent = Intent(this, Admin::class.java)
        startActivity(intent)
    }

}

