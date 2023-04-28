package com.example.live_a_little

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth

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

        val username = usernameList[position]
        val achievementNames = achievements[username]

        holder.homeCardDesign.removeAllViews()

        if (achievementNames != null) {
            if (holder.titleCardView.childCount == 1 && achievementNames.isNotEmpty()) {
                val titleCardView = createTitleCard(username)
                holder.homeCardDesign.addView(titleCardView)
            }
        }

        val layout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layout.setMargins(0, 10, 17, 10)
        layout.gravity = Gravity.END

        val achievementCardView = LinearLayout(context)
        achievementCardView.orientation = LinearLayout.VERTICAL

        holder.homeCardDesign.addView(achievementCardView)

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
        val view = LayoutInflater.from(context).inflate(R.layout.home_title, null)
        val titleTextView = view.findViewById<TextView>(R.id.textViewTitle)
        titleTextView.text = "$username's monthly achievements!"

        return view
    }

    private fun createAchievementCard(achievement: String): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.home_achievement, null)
        val achievementTitleTextView = view.findViewById<TextView>(R.id.textViewAchievementTitle)
        achievementTitleTextView.text = achievement

        return view
    }
}