package com.example.journalapp

import com.google.firebase.Timestamp

data class JournalData(
    var title: String = "",
    var thoughts: String = "",
    var timeAdded: Timestamp? = null,
    var imageUrl: String ="",
    var username: String = "",
    var userId: String = ""
)
