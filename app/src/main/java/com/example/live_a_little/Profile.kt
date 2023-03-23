package com.example.live_a_little

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import com.google.firebase.auth.FirebaseAuth

class Profile : AppCompatActivity() {

    private lateinit var btnHome: ImageButton
    private lateinit var btnAchievement: ImageButton
    private lateinit var btnLogout: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var txtDeleteAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        btnHome= findViewById(R.id.home_button)
        btnAchievement= findViewById(R.id.achievements_button)
        btnLogout= findViewById(R.id.btnLogout)
        txtDeleteAccount = findViewById(R.id.delete_account_text)

        btnHome.setOnClickListener{
            openHome()
        }

        btnAchievement.setOnClickListener{
            openAchievements()
        }

        btnAchievement.setOnClickListener{
            openAchievements()
        }

        btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut();
            openLogin()
        }

        txtDeleteAccount.setOnClickListener{
            deleteAccountPopup()
        }
    }

    private fun openHome(){
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    private fun openAchievements(){
        val intent = Intent(this, Achievements::class.java)
        startActivity(intent)
    }

    private fun openLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun deleteAccountPopup(){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_delete_confirmation, null)

        val focusable = true // let user click outside to dismiss
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            focusable
        )

        popupWindow.showAtLocation(txtDeleteAccount, Gravity.CENTER, 0, 0)

        val btnNo = popupView.findViewById<AppCompatButton>(R.id.btnDeleteAccountNo)
        val btnYes = popupView.findViewById<AppCompatButton>(R.id.btnDeleteAccountYes)

        btnNo.setOnClickListener{
            popupWindow.dismiss()
        }

        btnYes.setOnClickListener{
            Log.d("Test: ", "Yes button clicked")
        }

    }

}