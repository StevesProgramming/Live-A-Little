package com.example.live_a_little

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import androidx.recyclerview.widget.LinearLayoutManager

class Achievements : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var achievementNameList = ArrayList<String>()
    private var achievementDescList = ArrayList<String>()
    private lateinit var adapter: AchievementsAdapter
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        // Initalise recycle view and adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@Achievements)
        adapter = AchievementsAdapter(achievementNameList, achievementDescList, this@Achievements)
        recyclerView.adapter = adapter


        // Get data from the firebase realtime database
        dbRef =
            FirebaseDatabase.getInstance("https://project-cw-34e62-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Achievements")

        dbRef.addValueEventListener(object : ValueEventListener {
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
    }
}