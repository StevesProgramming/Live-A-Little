package com.example.live_a_little

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Profile : AppCompatActivity()  {

    private lateinit var btnHome: ImageButton
    private lateinit var btnAchievement: ImageButton
    private lateinit var btnLogout: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var txtDeleteAccount: TextView
    private var usernameList = ArrayList<String>()
    private var friendsNameList = ArrayList<String>()
    private var userIDList = ArrayList<String>()
    private lateinit var adapter: FriendsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initalise the views on the screen
        firebaseAuth = FirebaseAuth.getInstance()
        btnHome= findViewById(R.id.home_button)
        btnAchievement= findViewById(R.id.achievements_button)
        btnLogout= findViewById(R.id.btnLogout)
        txtDeleteAccount = findViewById(R.id.delete_account_text)
        searchView = findViewById(R.id.search_bar)
        // Check the user is logged in
        if (firebaseAuth.currentUser != null) {
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
                logout()
            }

            txtDeleteAccount.setOnClickListener{
                deleteAccountPopup()
            }

            // Take search from search bar and send to addFriend()
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(search: String?): Boolean {
                    if(!search.isNullOrEmpty()){
                        addFriend(search)

                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })

            contentChangeListener()

        } else {
            logout()
        }
    }
    private fun logout(){
        // Take user back to home screen and delete activity history in
        // the event they are not logged into an account while on the screen
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun openHome(){
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    private fun openAchievements(){
        val intent = Intent(this, Achievements::class.java)
        startActivity(intent)
    }

    private fun contentChangeListener() {
        // Listen for changes in the user collection and updates the screen on change
        val db = Firebase.firestore
        val user_id = firebaseAuth.uid.toString()
        val friends = db.collection("users")
            .document(user_id).collection("friends")

        friends.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            populateFriends()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun populateFriends(){
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        val userId = firebaseAuth.uid.toString();

        val friends = db.collection("users").document(userId)
            .collection("friends")

        usernameList.clear()
        userIDList.clear()

        // Initialise recycle view and adapter
        recyclerView = findViewById(R.id.friendRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@Profile)

        adapter = FriendsAdapter(usernameList, userIDList, this@Profile)
        recyclerView.adapter = adapter

        // Go through all the friends in the users friends list and store the details of each
        // into a list to display on screen later
        friends
            .get()
            .addOnCompleteListener { friendsDocuments ->
                if (friendsDocuments.isSuccessful) {
                    for (document in friendsDocuments.result) {
                        val friendsData = FriendsModel(document)

                        val friendUsername = friendsData.username
                        val friendID = document.id

                        if (friendUsername != null) {
                            usernameList.add(friendUsername)
                            userIDList.add(friendID)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }
    private fun addFriend(username: String){
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        val userId = firebaseAuth.uid.toString();

        val users = db.collection("users")

        val friends = db.collection("users").document(userId)
            .collection("friends")

        // Clears list to ensure correct information is stored
        friendsNameList.clear()

        // gets all friends already present in the users friends list
        friends
            .whereEqualTo("username", username)
            .get()
            .addOnCompleteListener { friendsDocuments ->
                if(friendsDocuments.isSuccessful){
                    val friend = friendsDocuments.result

                    // If the friend is empty the user is not friends with them.
                    if(friend.isEmpty){
                        // Get all users from the users collection who match the username entered
                        // in the search bar
                        users
                            .whereEqualTo("username", username)
                            .get()
                            .addOnCompleteListener { usersDocuments ->
                                if (usersDocuments.isSuccessful) {
                                    if(friend.isEmpty){
                                        for (user in usersDocuments.result) {
                                            val userData = UserModel(user)
                                            followUser(userData)
                                        }
                                    }
                                }
                            }
                    }
                    // If the friend is not empty the user is already friends with them
                    else{
                        Toast.makeText(this,
                            "Already following user!",
                            Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun followUser(userData: UserModel) {
        // Find the username entered in the search bar which is in the users collection
        // add their username and userID to the user's friends sub collection
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        val userId = firebaseAuth.uid.toString();

        val friends = db.collection("users").document(userId)
            .collection("friends")

        val friendData = hashMapOf(
            "username" to userData.username,
            "userID" to userData.userId
        )

        friends.add(friendData)

    }

    fun deleteAccountPopup(){
        // Dynamically build the popup window when trying to delete user account to request confirmation
        // from the user that it was intentional
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_delete_confirmation, null)

        val focusable = true
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            focusable
        )

        popupWindow.showAtLocation(txtDeleteAccount, Gravity.CENTER, 0, 0)

        val btnNo = popupView.findViewById<AppCompatButton>(R.id.btnDeleteAccountNo)
        val btnYes = popupView.findViewById<AppCompatButton>(R.id.btnDeleteAccountYes)

        btnNo.setOnClickListener{
            popupWindow.dismiss()
        }

        btnYes.setOnClickListener{
            removeUserFromFirebaseAuthentication()
            removeUserFromFirestore()
        }

    }

    private fun removeUserFromFirebaseAuthentication(){
        // Remove personal information from the firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser!!
        user.delete()
    }

    private fun removeUserFromFirestore(){
        // Remove personal information from the firestore
        firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.uid.toString();
        val db = Firebase.firestore

        val user = db.collection("users")
            .document(userId)

        val achievementSubCollection = db.collection("users")
            .document(userId)
            .collection("user_achievements")

        val friendSubCollection = db.collection("users")
            .document(userId)
            .collection("friends")


        achievementSubCollection
            .get()
            .addOnCompleteListener { achievements ->
                for (achievement in achievements.result) {
                    achievementSubCollection.document(achievement.id).delete()
                }
            }

        friendSubCollection
            .get()
            .addOnCompleteListener { friends ->
                for (friend in friends.result) {
                    friendSubCollection.document(friend.id).delete()
                }
            }
        user.delete()
    }
}




