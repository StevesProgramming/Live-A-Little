package com.example.live_a_little

import com.google.firebase.firestore.DocumentSnapshot
import java.security.Timestamp
import java.time.LocalDate
import java.util.*

data class FriendsModel(
    var username: String = ""
) {
    constructor(friends_document: DocumentSnapshot) : this(
        friends_document.getString("username")?: ""
    )
}
