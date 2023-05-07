package com.example.live_a_little

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class Admin : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnLogout: Button
    private var usernameList = ArrayList<String>()
    private var userIDList = ArrayList<String>()
    private lateinit var adapter: AdminAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        btnLogout= findViewById(R.id.btnLogout)
        searchView = findViewById(R.id.search_bar)

        // Initialise Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Check the user is logged in
        if (firebaseAuth.currentUser != null) {

            contentChangeListener()

            btnLogout.setOnClickListener {
                logout()
            }

            // Take search from search bar and send to deleteUserAccount()
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(search: String?): Boolean {
                    if (!search.isNullOrEmpty()) {
                        deleteUserAccount(search)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
        else {
            logout()
        }
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // Listen for changes in the user collection and updates the screen on change
    private fun contentChangeListener() {
        val db = Firebase.firestore
        val users = db.collection("users")

        users.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            populateUsers()
        }
    }

    private fun populateUsers(){
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        val users = db.collection("users")

        usernameList.clear()
        userIDList.clear()

        // Initialise recycle view and adapter
        recyclerView = findViewById(R.id.userRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@Admin)

        adapter = AdminAdapter(usernameList, userIDList, this@Admin)
        recyclerView.adapter = adapter

        // Get the current time
        val currentDate = Timestamp.now()
        // Set a time of 2 years
        val twoYears =
            Timestamp(currentDate.seconds - 30 * 24 * 60 * 60, 0)

        // Create a default time. In the DB the time for all users on signup is set to
        // 01/01/2016 00:00:01
        // If this time is found it means the user has not logged into the account
        // this means they have no validated their email and can be considered inactive
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

        // get all users who have been inactive for 2 years
        users
            .whereLessThan("last_active", twoYears)
            .get()
            .addOnCompleteListener { usersDocuments ->
                if (usersDocuments.isSuccessful) {
                    for (document in usersDocuments.result) {
                        val usersData = UserModel(document)

                        val userUsername = usersData.username
                        val userID = document.id

                        if (userUsername != null) {
                            usernameList.add(userUsername)
                            userIDList.add(userID)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

        // Get all users who have the same last active time of 01/01/2016 00:00:01
        // these users are unverified users
        users
            .whereEqualTo("last_active", cal.time)
            .get()
            .addOnCompleteListener { usersDocuments ->
                if (usersDocuments.isSuccessful) {
                    for (document in usersDocuments.result) {
                        val usersData = UserModel(document)

                        val userUsername = usersData.username
                        val userID = document.id

                        if (userUsername != null) {
                            usernameList.add(userUsername)
                            userIDList.add(userID)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }

    // Deletes account based on userId from firestore
    private fun deleteUserAccount(username: String){
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        val users = db.collection("users")

        // Get all users who match the name entered into the search bar
        users
            .whereEqualTo("username", username)
            .get()
            .addOnCompleteListener { usersDocuments ->
                if(usersDocuments.isSuccessful){
                    val userFound = usersDocuments.result

                    if(userFound.isEmpty){
                        Toast.makeText(this,
                            "User does not exist!",
                            Toast.LENGTH_LONG).show()
                    }
                    else{
                        // Send userID to the deleteSubCollections() function
                        for(document in userFound.documents){
                            val userId = document.id
                            deleteSubCollections(userId)
                        }

                    }

                }
            }
    }

    private fun deleteSubCollections(userID: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        val user = db.collection("users")
            .document(userID)

        val achievementSubCollection = db.collection("users")
            .document(userID)
            .collection("user_achievements")

        val friendSubCollection = db.collection("users")
            .document(userID)
            .collection("friends")

        // Delete the contents of the achievements sub collections
        achievementSubCollection
            .get()
            .addOnCompleteListener { achievements ->
                for (achievement in achievements.result) {
                    achievementSubCollection.document(achievement.id).delete()
                }
            }

        // Delete the contents of the friends sub collections
        friendSubCollection
            .get()
            .addOnCompleteListener { friends ->
                for (friend in friends.result) {
                    friendSubCollection.document(friend.id).delete()
                }
            }

        // Delete the user document
        user.delete()
    }

    private fun openLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}