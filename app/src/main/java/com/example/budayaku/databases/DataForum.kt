package com.example.budayaku.databases

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class DataForum(
    val username: String = "",
    val topik: String = "",
    val deskripsi: String = "",
    val user_avatar: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)