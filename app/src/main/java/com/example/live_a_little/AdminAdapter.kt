package com.example.live_a_little

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
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

class AdminAdapter(
    var usernameList: ArrayList<String>,
    var userIDList: ArrayList<String>,
    var context: Context) : RecyclerView.Adapter<AdminAdapter.FriendViewHolder>() {

    private lateinit var firebaseAuth: FirebaseAuth
    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDelete: TextView = itemView.findViewById(R.id.textDelete)
        var textUsername: TextView = itemView.findViewById(R.id.textUsername)
        var cardView: CardView = itemView.findViewById(R.id.friendView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.users_design, parent, false
        )
        return FriendViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.textUsername.text = usernameList[position]
        holder.textDelete.setOnClickListener {

            firebaseAuth = FirebaseAuth.getInstance()
            val userID = userIDList[position]

            // Inflater give the ability to convert our xml layouts to objects to dynamically build
            // the display of the recycler view
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View

            popupView = inflater.inflate(R.layout.popup_delete_user_confirmation, null)

            // Designing the display of the popup window
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.MATCH_PARENT
            val focusable = true // let user click outside to dismiss
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            // displaying the popup window on screen
            popupWindow.showAtLocation(holder.cardView, Gravity.CENTER, 0, 0)
            popupWindow.setFocusable(false);

            val btnDeleteYes = popupView.findViewById<AppCompatButton>(R.id.btnDeleteYes)
            val btnDeleteNo = popupView.findViewById<AppCompatButton>(R.id.btnDeleteNo)

            btnDeleteYes.setOnClickListener {
                deleteSubCollections(userID)
                popupWindow.dismiss()
            }

            btnDeleteNo.setOnClickListener {
                popupWindow.dismiss()
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

    override fun getItemCount(): Int {
        return usernameList.size
    }
}