package com.example.live_a_little

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class Signup : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etConfirmEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var btnBack: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var Firestore: FirebaseFirestore

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        Firestore = FirebaseFirestore.getInstance()

        etUsername = findViewById(R.id.usernameInput)
        etEmail = findViewById(R.id.emailInput)
        etConfirmEmail = findViewById(R.id.confirmEmailInput)
        etPassword = findViewById(R.id.passwordInput)
        etConfirmPassword = findViewById(R.id.confirmPasswordInput)

        btnSignup = findViewById(R.id.btnLogout)
        btnBack= findViewById(R.id.signup_back_button)

        btnBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnSignup.setOnClickListener{
            saveUserData()
        }
    }

    private fun saveUserData() {
        firebaseAuth = FirebaseAuth.getInstance()

        // getting values
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val confirmEmail = etConfirmEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (username.isEmpty()) {
            etUsername.error = "Please enter a username"
        } else if (email.isEmpty()) {
            etEmail.error = "Please enter an email"
        } else if (confirmEmail.isEmpty()) {
            etConfirmEmail.error = "Please enter an email"
        } else if (password.isEmpty()) {
            etPassword.error = "Please enter a password"
        } else if (confirmPassword.isEmpty()) {
            etConfirmPassword.error = "Please enter a password"
        } else {
            if(password == confirmPassword && email == confirmEmail){
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{
                    val user_id = firebaseAuth.uid.toString();
                    val user_name = username
                    val user_email = firebaseAuth.currentUser?.email.toString()
                    val current_user = firebaseAuth.currentUser

                    val users = db.collection("users")
                    val achievements = db.collection("achievements")

                    val user_details = hashMapOf(
                        "userID" to user_id,
                        "username" to user_name,
                        "email" to user_email
                    )

                    if(it.isSuccessful){
                        users.document(user_id).set(user_details)
                        achievements
                            .get()
                            .addOnSuccessListener { achievement_documents ->
                                for (document in achievement_documents) {
                                    db.collection("users")
                                        .document(user_id)
                                        .collection("user_achievements")
                                        .add(document)
                                }
                            }
                        current_user!!.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    lastActive()
                                    Toast.makeText(this,
                                        "Email Verification Sent",
                                        Toast.LENGTH_LONG).show()
                                }
                                else{
                                    Toast.makeText(this,
                                        "Error: Email Verification Not Sent",
                                        Toast.LENGTH_LONG).show()
                                }
                            }

                        Toast.makeText(this,
                            "Signup Successful",
                            Toast.LENGTH_LONG).show()
                        openLogin()
                    }
                    else{
                        Toast.makeText(this,
                            it.exception.toString(),
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
            else if (password != confirmPassword) {
                etConfirmPassword.error = "Passwords do not match"
            }
            else if (email != confirmEmail) {
                etConfirmEmail.error = "Emails do not match"
            }
        }
    }
    private fun openLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun lastActive(){
        firebaseAuth = FirebaseAuth.getInstance()
        val user_id = firebaseAuth.uid.toString();
        val db = Firebase.firestore

        val user = db.collection("users").document(user_id)

        val year = 2016
        val month = 0
        val day = 1
        val hour = 0
        val minute = 0
        val second = 1

        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, day)
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, second)

        val timestamp = cal.time
        val update = hashMapOf<String, Any>(
            "last_active" to timestamp
        )
        user.update(update)
    }
}