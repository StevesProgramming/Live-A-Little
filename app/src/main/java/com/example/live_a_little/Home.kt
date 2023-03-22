package com.example.live_a_little

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth

class Home : AppCompatActivity() {
    private lateinit var btnAchievement: ImageButton
    private lateinit var btnProfile: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        firebaseAuth = FirebaseAuth.getInstance()
        btnAchievement= findViewById(R.id.achievements_button)
        btnProfile= findViewById(R.id.profile_button)

        btnAchievement.setOnClickListener{
            openAchievements()
        }

        btnProfile.setOnClickListener{
            openProfile()
        }
    }

    private fun openAchievements(){
        val intent = Intent(this, Achievements::class.java)
        startActivity(intent)
    }

    private fun openProfile(){
        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
    }
}