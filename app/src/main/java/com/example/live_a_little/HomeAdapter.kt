package com.example.live_a_little

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class HomeAdapter(
    var usernameList: ArrayList<String>,
    var achievements: MutableMap<Any, ArrayList<String>>,
    var context: Context) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val titleCardView: CardView = itemView.findViewById(R.id.titleCardView)
        val homeCardDesign: LinearLayout = itemView.findViewById(R.id.homeCardDesign)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.home_card_design
            , parent
            , false)
        return HomeViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        // Get the username and achievement name based on location on the recycler view
        val username = usernameList[position]
        val achievementNames = achievements[username]

        // Remove all default views
        holder.homeCardDesign.removeAllViews()

        // Limit the titleCard to be output once instead of for every achievement
        if (achievementNames != null) {
            if (holder.titleCardView.childCount == 1 && achievementNames.isNotEmpty()) {
                val titleCardView = createTitleCard(username)
                holder.homeCardDesign.addView(titleCardView)
            }
        }

        // Hard code the design of the achievement views creation
        val layout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layout.setMargins(0, 10, 17, 10)
        layout.gravity = Gravity.END

        val achievementCardView = LinearLayout(context)
        achievementCardView.orientation = LinearLayout.VERTICAL

        holder.homeCardDesign.addView(achievementCardView)

        // Add a achievement card for every achievement in the dictionary associated with the
        // userId as the key
        if (achievementNames != null) {
            for (achievementName in achievementNames) {
                val achievementCard = createAchievementCard(achievementName)
                if (achievementCard != null) {
                    achievementCard.layoutParams = layout
                }
                achievementCardView.addView(achievementCard)
            }
        }
    }

    override fun getItemCount(): Int {
        return usernameList.size
    }

    @SuppressLint("SetTextI18n")
    private fun createTitleCard(username: String): View? {
        // Code to create the title card views
        val view = LayoutInflater.from(context).inflate(R.layout.home_title, null)
        val titleTextView = view.findViewById<TextView>(R.id.textViewTitle)
        titleTextView.text = "$username's monthly achievements!"

        return view
    }

    private fun createAchievementCard(achievement: String): View? {
        // Code to create the achievement card views
        val view = LayoutInflater.from(context).inflate(R.layout.home_achievement, null)
        val achievementTitleTextView = view.findViewById<TextView>(R.id.textViewAchievementTitle)
        achievementTitleTextView.text = achievement

        return view
    }
}