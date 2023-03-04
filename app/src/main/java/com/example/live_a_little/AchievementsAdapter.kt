package com.example.live_a_little

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.live_a_little.R
import de.hdodenhof.circleimageview.CircleImageView

class AchievementsAdapter(
    var titleNameList: ArrayList<String>,
    var descList: ArrayList<String>,
    //var imageList: ArrayList<Int>,
    var context: Context) : RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder>() {

    class AchievementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var textViewTitle : TextView = itemView.findViewById(R.id.textViewTitle)
        var textViewDesc : TextView = itemView.findViewById(R.id.textViewDesc)
        //var imageView : CircleImageView = itemView.findViewById(R.id.imageView)
        var cardView : CardView = itemView.findViewById(R.id.cardView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {

        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.card_design
            , parent
            , false)
        return AchievementViewHolder(view)

    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {

        holder.textViewTitle.text = titleNameList[position]
        holder.textViewDesc.text = descList[position]
        //holder.imageView.setImageResource((imageList[position]))

        holder.cardView.setOnClickListener {

            Toast.makeText(context, "You selected the ${titleNameList[position]}", Toast.LENGTH_SHORT).show()

        }

    }

    override fun getItemCount(): Int {

        return titleNameList.size

    }

}