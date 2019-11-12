package com.example.budayaku.databases

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class DataForum(
    val user_id: String = "",
    val topik: String = "",
    val deskripsi: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)