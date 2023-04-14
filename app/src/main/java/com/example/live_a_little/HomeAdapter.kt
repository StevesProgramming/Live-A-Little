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
    //var achievementNameList: ArrayList<String>,
    var achievements: MutableMap<Any, ArrayList<String>>,
    var context: Context) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private lateinit var firebaseAuth: FirebaseAuth
    var adapter: HomeAdapter? = null

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var textViewAchievementTitle: TextView = itemView.findViewById(R.id.textViewAchievementTitle)
        val titleCardView: CardView = itemView.findViewById(R.id.titleCardView)
        //val designLayout: CardView = itemView.findViewById(R.id.designLayout)
        val achievementCardView: CardView = itemView.findViewById(R.id.achievementCardView)
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

        //val linearLayout = LinearLayout(context)
        //linearLayout.orientation = LinearLayout.VERTICAL

        if (holder.titleCardView.childCount == 1) {
            val titleCardView = createTitleCard(username)
            holder.homeCardDesign.addView(titleCardView)
        }

        val layout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layout.setMargins(0, 10, 17, 10)
        layout.gravity = Gravity.END

        val achievementCardView = LinearLayout(context)
        achievementCardView.orientation = LinearLayout.VERTICAL

        // Apply the layout parameters to the achievement card view
        //achievementCardView.layoutParams = layout

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


//        for ((username, achievement) in achievements) {
//            println("Key: $username")
//            println("Values: $achievement")
//            for (value in achievement) {
//                println(value)
//                val achievementCardView = createAchievementCard(value)
//                holder.achievementCardView.addView(achievementCardView)
//            }
//        }




//        if (achievementName != null) {
//            for (achievement in achievementName) {
//                val achievementCardView = createAchievementCard(achievement)
//                holder.achievementCardView.addView(achievementCardView)
//            }
//
//        }

//        achievements.forEach { achievement ->
//            val achievementCardView = createAchievementCard(achievement.toString())
//            holder.achievementCardView.addView(achievementCardView)
//        }





//        if(usernameList != null) {
//            for(user in usernameList){
//
//                holder.titleCardView.visibility = View.VISIBLE
//                holder.textViewTitle.text = username
//
//                if (achievementName != null) {
//                    for (achievement in achievementName) {
//                        // Inflate the layout for the achievement
//                        val achievementLayout = LayoutInflater.from(context)
//                            .inflate(R.layout.home_card_design, holder.achievementCardView, false)
//
//                        // Set the text and image for the achievement layout
//                        achievementLayout.findViewById<TextView>(R.id.textViewAchievementTitle)
//                            .text = achievement
//                        achievementLayout.findViewById<ImageView>(R.id.imageView3)
//                            .setImageResource(R.drawable.logo)
//
//                        // Add the achievement layout to the designLayout
//                        holder.achievementCardView.addView(achievementLayout)
//                    }
//                }
//            }
//
//        }




//        Log.d("Testing", "2" + achievements.toString())
//
//        val previousName = ""
//        for((user, achievement) in achievements){
//            holder.titleCardView.visibility = View.VISIBLE
//            holder.textViewTitle.text = user.toString()
//
//            val list = achievements[user]
//
//            if (list != null) {
//                for(achievment in list){
//                    holder.textViewAchievementTitle.text = achievment
//                }
//            }
//        }

//        if (position == 0 || achievementNameList[position] != achievementNameList[position-1]) {
//            // This is the first achievement in the list or a new achievement, so show the titleCardView
//            holder.titleCardView.visibility = View.VISIBLE
//            holder.textViewTitle.text = usernameList[position]
//            //holder.textViewTitleDate.text = currentDate
//        } else {
//            // This is not the first achievement in the list and belongs to the same achievement as the previous one, so hide the titleCardView
//            holder.titleCardView.visibility = View.GONE
//        }
//
//        //holder.textViewTitle.text = usernameList[position]
//        holder.textViewAchievementTitle.text = achievementNameList[position]
    }

    override fun getItemCount(): Int {
        return usernameList.size
    }

    private fun createTitleCard(username: String): View? {
        //val view = LayoutInflater.from(context).inflate(R.layout.home_card_design, null)
        val view = LayoutInflater.from(context).inflate(R.layout.home_title, null)

        Log.d("Username",  username)
        val titleTextView = view.findViewById<TextView>(R.id.textViewTitle)
        titleTextView.text = username

        return view
    }

    private fun createAchievementCard(achievement: String): View? {
        //val view = LayoutInflater.from(context).inflate(R.layout.home_card_design, null)
        val view = LayoutInflater.from(context).inflate(R.layout.home_achievement, null)

        val achievementTitleTextView = view.findViewById<TextView>(R.id.textViewAchievementTitle)
        achievementTitleTextView.text = achievement

        return view
    }

}