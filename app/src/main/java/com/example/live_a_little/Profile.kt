package com.example.live_a_little

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private var achievementsNameList = ArrayList<String>()
    private var userIDList = ArrayList<String>()
    private lateinit var adapter: FriendsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        btnHome= findViewById(R.id.home_button)
        btnAchievement= findViewById(R.id.achievements_button)
        btnLogout= findViewById(R.id.btnLogout)
        txtDeleteAccount = findViewById(R.id.delete_account_text)
        searchView = findViewById(R.id.search_bar)



        populateFriends()

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

        txtDeleteAccount.setOnClickListener{
            deleteAccountPopup()
        }

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

    fun deleteAccountPopup(){
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
            Log.d("Test: ", "Yes button clicked")
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

        friends
            .get()
            .addOnCompleteListener { friendsDocuments ->
                if (friendsDocuments.isSuccessful) {
                    for (document in friendsDocuments.result) {
                        val friendsData = FriendsModel(document)

                        val friendId = document.id
                        val friendUsername = friendsData.username
                        val friendID = document.id

                        if (friendUsername != null) {
                            usernameList.add(friendUsername)
                            userIDList.add(friendID)
                        }
                        Log.d("Friends List", friendsData.toString())
                        Log.d("Friends List", usernameList.toString())
                    }
                    adapter.notifyDataSetChanged()
                }
                else{
                    Log.d(ContentValues.TAG, "Error getting documents: ", friendsDocuments.exception)
                }
            }

    }
    private fun addFriend(username: String){
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        val userId = firebaseAuth.uid.toString();

        val users = db.collection("users")
        var userFriendMatch = false;

        val friends = db.collection("users").document(userId)
            .collection("friends")

        achievementsNameList.clear()

        friends
            .whereEqualTo("username", username)
            .get()
            .addOnCompleteListener { friendsDocuments ->
                if(friendsDocuments.isSuccessful){
                    val friend = friendsDocuments.result

                    if(friend.isEmpty){
                        users
                            .whereEqualTo("username", username)
                            .get()
                            .addOnCompleteListener { usersDocuments ->
                                if (usersDocuments.isSuccessful) {
                                    val user = usersDocuments.result

                                    if(friend.isEmpty){
                                        for (user in usersDocuments.result) {
                                            val userData = UserModel(user)
                                            Log.d("Testing", userData.userId)

                                            followUser(username, userData)
                                        }
                                    }
                                }

                            }
                    }
                    else{
                        Toast.makeText(this,
                            "Already following user!",
                            Toast.LENGTH_LONG).show()
                    }
                }

        }
    }



//    private fun addFriend(username: String){
//        firebaseAuth = FirebaseAuth.getInstance()
//        val db = Firebase.firestore
//        val userId = firebaseAuth.uid.toString();
//
//        val users = db.collection("users")
//        var userFriendMatch = false;
//
//        val friends = db.collection("users").document(userId)
//            .collection("friends")
//
//        friends
//            .get()
//            .addOnCompleteListener { friendsDocuments ->
//
//                for (friend in friendsDocuments.result) {
//                    val friendsData = FriendsModel(friend)
//
//                    if(username == friendsData.username){
//                        Toast.makeText(this, "Following User!", Toast.LENGTH_LONG).show()
//                    }
//                    else{
//                        users
//                            .get()
//                            .addOnCompleteListener { usersDocuments ->
//                                if (usersDocuments.isSuccessful) {
//                                    for (user in usersDocuments.result) {
//                                        val userData = UserModel(user)
//                                        val usersUsername = userData.username
//
//                                        Log.d( "usersUsername:", usersUsername)
//                                        Log.d( "userData.username:", userData.username)
//
//                                        if(username == usersUsername){
//                                            followUser(username, userData)
//                                        }
//                                        else{
//                                            Toast.makeText(this, "User does not exist", Toast.LENGTH_LONG).show()
//                                        }
//                                    }
//                                }
//                                else{
//                                    Log.d(ContentValues.TAG, "Error getting documents: ", usersDocuments.exception)
//                                }
//                            }
//                    }
//                }
//        }
//    }

    private fun followUser(username: String, userData: UserModel) {
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
}




