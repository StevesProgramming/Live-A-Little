package com.example.live_a_little

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
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.live_a_little.R
import de.hdodenhof.circleimageview.CircleImageView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AchievementsAdapter(
    var nameList: ArrayList<String>,
    var descList: ArrayList<String>,
    var completeList: ArrayList<Boolean>,
    var goalList: ArrayList<Int>,
    var successfullyCompleteList: ArrayList<Int>,
    var context: Context) : RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder>() {

    private lateinit var firebaseAuth: FirebaseAuth
    class AchievementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var textViewDesc: TextView = itemView.findViewById(R.id.textViewDesc)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.card_design
            , parent
            , false)
        return AchievementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.textViewTitle.text = nameList[position]
        holder.textViewDesc.text = descList[position]
        holder.cardView.setOnClickListener {

            val goal = goalList[position]
            val complete = completeList[position]

            Log.d("Goal Number: ", goal.toString())

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View

            // deciding which popup to display based on whether the achievement
            // is an incremental achievement or single task achievement
            if(complete == false){
                if(goal > 1){
                    popupView = inflater.inflate(R.layout.popup_window_increment, null)
                } else{
                    popupView = inflater.inflate(R.layout.popup_window, null)
                }
            } else{
                popupView = inflater.inflate(R.layout.popup_window_remove, null)
            }

            // Designing the display of the popup window
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.MATCH_PARENT
            val focusable = true // let user click outside to dismiss
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            // displaying the popup window on screen
            popupWindow.showAtLocation(holder.cardView, Gravity.CENTER, 0, 0)
            popupWindow.setFocusable(false);

            // set up the title and description in popup
            val textViewTitle = popupView.findViewById<TextView>(R.id.textViewTitle)
            val textViewDesc = popupView.findViewById<TextView>(R.id.textViewDesc)
            textViewTitle.text = nameList[position]
            textViewDesc.text = descList[position]


            // enable close button
            val closeButton = popupView.findViewById<AppCompatImageButton>(R.id.btnClose)
            closeButton.setOnClickListener {
                popupWindow.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        return nameList.size
    }


}