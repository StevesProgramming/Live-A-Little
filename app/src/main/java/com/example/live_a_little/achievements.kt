package com.example.live_a_little

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Achievements : AppCompatActivity() {

    private lateinit var btnHome: ImageButton
    private lateinit var btnProfile: ImageButton
    private lateinit var btnComplete: Button
    private lateinit var btnIncomplete: Button
    private lateinit var recyclerView: RecyclerView
    private var achievementIDList = ArrayList<String>()
    private var achievementNameList = ArrayList<String>()
    private var achievementDescList = ArrayList<String>()
    private var achievementCompleteList = ArrayList<Boolean>()
    private var achievementGoalList = ArrayList<Int>()
    private var achievementSuccessfulCompletionList = ArrayList<Int>()
    private lateinit var adapter: AchievementsAdapter
    private lateinit var achievementDbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var currentList: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        // Initialise Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialise Top Navigation
        btnComplete = findViewById(R.id.complete_button)
        btnIncomplete = findViewById(R.id.incomplete_button)

        // Initialise Bottom Navigation
        btnHome = findViewById(R.id.home_button)
        btnProfile = findViewById(R.id.profile_button)

        currentList = "incomplete";

        // Check the user is logged in
        if (firebaseAuth.currentUser != null) {

            populateAchievements(currentList)

            btnComplete.setOnClickListener {
                btnComplete.setBackgroundResource(R.drawable.frag_button_selected)
                btnComplete.setTextColor(ContextCompat.getColor(this, R.color.white))

                btnIncomplete.setBackgroundResource(R.drawable.frag_button_unselected)
                btnIncomplete.setTextColor(ContextCompat.getColor(this, R.color.black))

                currentList = "complete";
                populateAchievements(currentList)
            }

            btnIncomplete.setOnClickListener {
                btnIncomplete.setBackgroundResource(R.drawable.frag_button_selected)
                btnIncomplete.setTextColor(ContextCompat.getColor(this, R.color.white))

                btnComplete.setBackgroundResource(R.drawable.frag_button_unselected)
                btnComplete.setTextColor(ContextCompat.getColor(this, R.color.black))

                currentList = "incomplete";
                populateAchievements(currentList)
            }

            btnHome.setOnClickListener {
                openHome()
            }

            btnProfile.setOnClickListener {
                openProfile()
            }

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
    @SuppressLint("NotifyDataSetChanged")
    private fun populateAchievements(currentList: String){
        val db = Firebase.firestore
        val user_id = firebaseAuth.uid.toString();
        val achievements = db.collection("users").document(user_id)
            .collection("user_achievements")

        achievementIDList.clear()
        achievementNameList.clear()
        achievementDescList.clear()
        achievementCompleteList.clear()
        achievementGoalList.clear()
        achievementSuccessfulCompletionList.clear()

        // Initialise recycle view and adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@Achievements)

        adapter = AchievementsAdapter(achievementNameList, achievementDescList, achievementCompleteList,
            achievementGoalList, achievementSuccessfulCompletionList, achievementIDList, this@Achievements)
        recyclerView.adapter = adapter

        achievements
            .get()
            .addOnCompleteListener { achievement_documents ->
                if (achievement_documents.isSuccessful) {
                    for (document in achievement_documents.result) {
                        val achievement_data = AchievementsModel(document)

                        val achievementName = achievement_data.name
                        val achievementDesc = achievement_data.description
                        val achievementComplete = achievement_data.complete
                        val achievementGoal = achievement_data.goal
                        val achievementSuccessfulCompletion = achievement_data.successful_completions
                        val achievementId = document.id

                        if (achievementName != null && achievementDesc != null) {
                            if(currentList == "incomplete" && achievementComplete == false){
                                    achievementIDList.add(achievementId)
                                    achievementNameList.add(achievementName)
                                    achievementDescList.add(achievementDesc)
                                    achievementCompleteList.add(achievementComplete)
                                    achievementGoalList.add(achievementGoal)
                                    achievementSuccessfulCompletionList.add(achievementSuccessfulCompletion)
                            }
                            else if(currentList == "complete" && achievementComplete == true){
                                    achievementIDList.add(achievementId)
                                    achievementNameList.add(achievementName)
                                    achievementDescList.add(achievementDesc)
                                    achievementCompleteList.add(achievementComplete)
                                    achievementGoalList.add(achievementGoal)
                                    achievementSuccessfulCompletionList.add(achievementSuccessfulCompletion)
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                else{
                    Log.d(TAG, "Error getting documents: ", achievement_documents.exception)
                }
        }
     }
}