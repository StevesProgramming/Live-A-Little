package com.example.live_a_little

import com.google.firebase.firestore.DocumentSnapshot
import java.security.Timestamp
import java.time.LocalDate
import java.util.*

data class AchievementsModel(
    var name: String = "",
    var description: String = "",
    var complete: Boolean = false,
    var goal: Int = 0,
    var successful_completions: Int = 0
) {
    constructor(achievement_document: DocumentSnapshot) : this(
        achievement_document.getString("data.name")?: "",
        achievement_document.getString("data.description")?: "",
        achievement_document.getBoolean("data.complete")?: false,
        achievement_document.getLong("goal")?.toInt() ?: 0,
        achievement_document.getLong("successful_completions")?.toInt() ?: 0
    )
}
