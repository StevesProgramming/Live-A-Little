package com.example.live_a_little

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.recyclerview.widget.LinearLayoutManager

class Achievements : AppCompatActivity() {

    private lateinit var btnHome: ImageButton
    private lateinit var btnProfile: ImageButton

    private lateinit var recyclerView: RecyclerView
    private var achievementNameList = ArrayList<String>()
    private var achievementDescList = ArrayList<String>()
    private lateinit var adapter: AchievementsAdapter

    private lateinit var achievementDbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        // Initialise Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialise Navigation
        btnHome= findViewById(R.id.home_button)
        btnProfile= findViewById(R.id.profile_button)

        btnHome.setOnClickListener{
            openHome()
        }

        btnProfile.setOnClickListener{
            openProfile()
        }

        // Check the user is logged in
        if (firebaseAuth.currentUser != null) {

            // Initialise recycle view and adapter
            recyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this@Achievements)
            adapter = AchievementsAdapter(achievementNameList, achievementDescList, this@Achievements)
            recyclerView.adapter = adapter

            achievementDbRef =
                FirebaseDatabase.getInstance("https://project-cw-34e62-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("achievements")

            achievementDbRef.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (i in dataSnapshot.children) {
                        var achievementName = i.child("name").getValue(String::class.java)
                        var achievementDesc = i.child("description").getValue(String::class.java)

                        if (achievementName != null && achievementDesc != null) {
                            achievementNameList.add(achievementName)
                            achievementDescList.add(achievementDesc)
                        }
                    }

                    adapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        } else {
            openLogin()
            finish()
        }
    }

    private fun openLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun openHome(){
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    private fun openProfile(){
        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
    }
}