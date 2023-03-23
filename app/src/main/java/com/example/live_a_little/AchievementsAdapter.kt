package com.example.live_a_little

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
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth

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

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View

            val popupWindowType: String

            // deciding which popup to display based on whether the achievement
            // is an incremental achievement or single task achievement
            if(complete == false){
                if(goal > 1){
                    popupView = inflater.inflate(R.layout.popup_window_increment, null)
                    popupWindowType = "popup_window_increment"
                } else{
                    popupView = inflater.inflate(R.layout.popup_window, null)
                    popupWindowType = "popup_window"
                }
            }
            else{
                popupView = inflater.inflate(R.layout.popup_window_remove, null)
                popupWindowType = "popup_window_remove"
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

            // Popup logic
            if(popupWindowType == "popup_window_increment"){
                val btnIncrementPlus = popupView.findViewById<AppCompatButton>(R.id.btnIncrementPlus)
                val btnIncrementMinus = popupView.findViewById<AppCompatButton>(R.id.btnIncrementMinus)

                btnIncrementPlus.setOnClickListener {
                    Log.d("Test: ", "btnIncrementPlus button clicked")
                }

                btnIncrementMinus.setOnClickListener {
                    Log.d("Test: ", "btnIncremementMinus button clicked")
                }
            }
            else if(popupWindowType == "popup_window"){
                val btnComplete = popupView.findViewById<AppCompatButton>(R.id.btnComplete)

                btnComplete.setOnClickListener {
                    Log.d("Test: ", "btnComplete button clicked")
                }
            }
            else if(popupWindowType == "popup_window_remove"){
                val btnRemove = popupView.findViewById<AppCompatButton>(R.id.btnRemove)

                btnRemove.setOnClickListener {
                    Log.d("Test: ", "btnRemove button clicked")
                    popupWindow.dismiss()

                    val confirmInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val confirmPopupView = inflater.inflate(R.layout.popup_remove_confirmation, null)

                    // Designing the display of the popup window
                    val confirmPopupWindow = PopupWindow(confirmPopupView, width, height, focusable)

                    // displaying the popup window on screen
                    confirmPopupWindow.showAtLocation(holder.cardView, Gravity.CENTER, 0, 0)
                    confirmPopupWindow.setFocusable(false);

                    val btnNo = confirmPopupView.findViewById<AppCompatButton>(R.id.btnRemoveAchievementNo)
                    val btnYes = confirmPopupView.findViewById<AppCompatButton>(R.id.btnRemoveAchievementYes)

                    btnNo.setOnClickListener{
                        confirmPopupWindow.dismiss()
                    }

                    btnYes.setOnClickListener{
                        Log.d("Test: ", "Yes button clicked")
                    }
                }
            }

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