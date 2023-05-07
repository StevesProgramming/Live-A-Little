package com.example.live_a_little

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FriendsAdapter(
    var usernameList: ArrayList<String>,
    var userIDList: ArrayList<String>,
    var context: Context) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    private lateinit var firebaseAuth: FirebaseAuth

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textUnfollow: TextView = itemView.findViewById(R.id.textUnfollow)
        var textUsername: TextView = itemView.findViewById(R.id.textUsername)
        var cardView: CardView = itemView.findViewById(R.id.friendView)
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
        holder.textUnfollow.setOnClickListener {

            firebaseAuth = FirebaseAuth.getInstance()
            val db = Firebase.firestore
            val user_id = firebaseAuth.uid.toString();

            val userID = userIDList[position]

            val friends = db.collection("users").document(user_id)
                .collection("friends")

            val documentToRemove = friends.document(userID)

            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View

            popupView = inflater.inflate(R.layout.popup_unfollow_confirmation, null)

            // Designing the display of the popup window
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.MATCH_PARENT
            val focusable = true // let user click outside to dismiss
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            // displaying the popup window on screen
            popupWindow.showAtLocation(holder.cardView, Gravity.CENTER, 0, 0)
            popupWindow.setFocusable(false);

            val btnUnfollowYes = popupView.findViewById<AppCompatButton>(R.id.btnUnfollowYes)
            val btnUnfollowNo = popupView.findViewById<AppCompatButton>(R.id.btnUnfollowNo)

            btnUnfollowYes.setOnClickListener {
                documentToRemove.delete()
                popupWindow.dismiss()
            }

            btnUnfollowNo.setOnClickListener {
                popupWindow.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        return usernameList.size
    }
}