package com.example.live_a_little

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FriendsAdapter(
    var usernameList: ArrayList<String>,
    var context: Context) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    private lateinit var firebaseAuth: FirebaseAuth
    var adapter: FriendsAdapter? = null

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textUsername: TextView = itemView.findViewById(R.id.textUsername)
        var cardView: CardView = itemView.findViewById(R.id.friendView)
        var constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.friends_design, parent, false
        )
        return FriendViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.textUsername.text = usernameList[position]
        holder.constraintLayout.setOnClickListener {

            firebaseAuth = FirebaseAuth.getInstance()
            val db = Firebase.firestore
            val user_id = firebaseAuth.uid.toString();

            val friends = db.collection("users").document(user_id)
                .collection("friends")

            Toast.makeText(context, "Testing Friend", Toast.LENGTH_SHORT).show()


        }
    }

    override fun getItemCount(): Int {
        return usernameList.size
    }
}