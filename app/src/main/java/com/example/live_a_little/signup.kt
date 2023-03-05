package com.example.live_a_little

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etConfirmEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText

    private lateinit var btnSignup: Button
    private lateinit var btnBack: Button
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        etUsername = findViewById(R.id.usernameInput)
        etEmail = findViewById(R.id.emailInput)
        etConfirmEmail = findViewById(R.id.confirmEmailInput)
        etPassword = findViewById(R.id.passwordInput)
        etConfirmPassword = findViewById(R.id.confirmPasswordInput)

        btnSignup = findViewById(R.id.btnLogout)
        btnBack= findViewById(R.id.signup_back_button)
        dbRef = FirebaseDatabase.getInstance("https://project-cw-34e62-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")

        btnBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnSignup.setOnClickListener{
            saveUserData()
        }
    }

    private fun saveUserData() {
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
                val userId = dbRef.push().key!!
                val user = UserModel(userId, username, email, password)

                dbRef.child(userId).setValue(user).addOnCompleteListener {
                    Toast.makeText(this, "Signup successful", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else if (password != confirmPassword) {
                etConfirmPassword.error = "Passwords do not match"
            }
            else if (email != confirmEmail) {
                etConfirmEmail.error = "Emails do not match"
            }
        }
    }
}