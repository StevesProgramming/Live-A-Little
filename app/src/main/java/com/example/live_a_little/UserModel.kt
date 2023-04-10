package com.example.live_a_little

import com.google.firebase.firestore.DocumentSnapshot

data class UserModel(
    var userId: String = "",
    var username: String = "",
    var email: String = "",
) {
    constructor(user_document: DocumentSnapshot) : this(
        user_document.getString("userId")?: "",
        user_document.getString("username")?: "",
        user_document.getString("email")?: ""
    )
}