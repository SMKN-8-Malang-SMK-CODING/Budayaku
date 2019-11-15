package com.example.budayaku.databases

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class DataCommentForum(
    val user_id: String = "",
    val topik: String = "",
    val comment: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)