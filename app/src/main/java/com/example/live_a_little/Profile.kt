package com.example.live_a_little

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth

class Profile : AppCompatActivity() {

    private lateinit var btnHome: ImageButton
    private lateinit var btnAchievement: ImageButton
    private lateinit var btnLogout: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        btnHome= findViewById(R.id.home_button)
        btnAchievement= findViewById(R.id.achievements_button)
        btnLogout= findViewById(R.id.btnLogout)

        btnHome.setOnClickListener{
            openHome()
        }

        btnAchievement.setOnClickListener{
            openAchievements()
        }

        btnAchievement.setOnClickListener{
            openAchievements()
        }

        btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut();
            openLogin()
        }
    }

    private fun openHome(){
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    private fun openAchievements(){
        val intent = Intent(this, Achievements::class.java)
        startActivity(intent)
    }

    private fun openLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}