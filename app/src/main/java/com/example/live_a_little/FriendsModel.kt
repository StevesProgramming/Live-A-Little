package com.example.live_a_little

import com.google.firebase.firestore.DocumentSnapshot

data class FriendsModel(
    var username: String = ""
) {
    constructor(friends_document: DocumentSnapshot) : this(
        friends_document.getString("username")?: ""
    )
}
