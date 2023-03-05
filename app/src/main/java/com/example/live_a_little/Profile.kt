package com.example.live_a_little

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Profile : AppCompatActivity() {

    private lateinit var btnHome: ImageButton
    private lateinit var btnAchievement: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        btnHome= findViewById(R.id.home_button)
        btnAchievement= findViewById(R.id.achievements_button)

        btnHome.setOnClickListener{
            openHome()
        }

        btnAchievement.setOnClickListener{
            openAchievements()
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

}