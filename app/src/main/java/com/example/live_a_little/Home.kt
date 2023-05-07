package com.example.live_a_little

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class Home : AppCompatActivity() {
    private lateinit var btnAchievement: ImageButton
    private lateinit var btnProfile: ImageButton

    private var usernameList = ArrayList<String>()
    private val achievements: MutableMap<Any, ArrayList<String>> = HashMap()

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var achievementDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        firebaseAuth = FirebaseAuth.getInstance()
        btnAchievement = findViewById(R.id.achievements_button)
        btnProfile = findViewById(R.id.profile_button)

        // Check the user is logged in
        if (firebaseAuth.currentUser != null) {

            populateHome()

            btnAchievement.setOnClickListener {
                openAchievements()
            }

            btnProfile.setOnClickListener {
                openProfile()
            }
        }
        else{
            logout()
        }
    }

    private fun openAchievements() {
        val intent = Intent(this, Achievements::class.java)
        startActivity(intent)
    }

    private fun openProfile() {
        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun populateHome() {
        val db = Firebase.firestore
        val user_id = firebaseAuth.uid.toString();

        val friends = db.collection("users").document(user_id)
            .collection("friends")

        usernameList.clear()
        achievements.clear()

        // Initialise recycle view and adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@Home)

        adapter = HomeAdapter(usernameList, achievements, this@Home)
        recyclerView.adapter = adapter

        friends
            .get()
            .addOnCompleteListener { friends_documents ->
                if (friends_documents.isSuccessful) {
                    for (friendDocument in friends_documents.result) {
                        val userData = UserModel(friendDocument)
                        val userId = userData.userId
                        val username = userData.username

                        usernameList.add(username)

                        val users = db.collection("users").document(userId)
                            .collection("user_achievements")


                        val currentDate = Timestamp.now()

                        // Subtract 30 days from the current date
                        // days, hours, minute, second, nanoseconds
                        val thirtyDaysAgo =
                            Timestamp(currentDate.seconds - 30 * 24 * 60 * 60, 0)

                        // Get all users and achievements accomplished in the last 30 days
                        users
                            .whereGreaterThan("data.date", thirtyDaysAgo)
                            .get()
                            .addOnCompleteListener { users_documents ->
                                val achievementNameList = mutableListOf<String>()

                                for (userDocument in users_documents.result) {
                                    val achievement_data = AchievementsModel(userDocument)
                                    val achievementName = achievement_data.name

                                    achievementNameList.add(achievementName)
                                }

                                // Create a dictionary of usernames and achievements
                                // username is key and achievements list is value
                                achievements[username] = achievementNameList as ArrayList<String>
                                adapter.notifyDataSetChanged()
                            }
                    }
                }
            }
    }
}
