package com.example.live_a_little

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



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

        populateUsers()

        btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut();
            openLogin()
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(search: String?): Boolean {
                if(!search.isNullOrEmpty()){
                    deleteUser(search)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
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

        users
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

    private fun deleteUser(username: String){
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        val userId = firebaseAuth.uid.toString();
        val users = db.collection("users")

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
                        for (user in usersDocuments.result) {
                            val userData = UserModel(user)
                            val userID = userData.userId as String
                            val documentToRemove = db.collection("users").document(userId)

                            documentToRemove.delete()
                        }
                    }
                }

            }
    }

    private fun openLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}