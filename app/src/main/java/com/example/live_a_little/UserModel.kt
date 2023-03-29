package com.example.live_a_little

import com.google.firebase.firestore.DocumentSnapshot

data class UserModel(
    var userId: String = "",
    var username: String = "",
    var email: String = "",
) {
    constructor(user_document: DocumentSnapshot) : this(
        user_document.getString("data.name")?: "",
        user_document.getString("data.description")?: "",
        user_document.getString("data.complete")?: ""
    )
}