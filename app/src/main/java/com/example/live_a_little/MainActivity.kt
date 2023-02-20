package com.example.live_a_little

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var txtSignup: TextView

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsername = findViewById(R.id.usernameInput)
        etPassword = findViewById(R.id.passwordInput)
        dbRef = FirebaseDatabase.getInstance("https://project-cw-34e62-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")

        btnLogin= findViewById(R.id.btnLogin)
        txtSignup = findViewById(R.id.signup_text)

        btnLogin.setOnClickListener{
            login()
        }

        txtSignup.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    fun login(){
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (username.isEmpty()) {
            etUsername.error = "Please enter a username"
        } else if (password.isEmpty()) {
            etPassword.error = "Please enter a password"
        }
        else {
            //Get Data
            var getData = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError){}
                override fun onDataChange(p0: DataSnapshot){
                    for(i in p0.children){
                        var dbUsername = i.child("username").value
                        var dbPassword = i.child("password").value

                        if(username == dbUsername && password == dbPassword){
                            openAchievements()
                        }
                    }
                }
            }
            dbRef.addValueEventListener(getData)
            dbRef.addListenerForSingleValueEvent(getData)
        }
    }

    fun openAchievements(){
        val intent = Intent(this, Achievements::class.java)
        startActivity(intent)
    }
}
