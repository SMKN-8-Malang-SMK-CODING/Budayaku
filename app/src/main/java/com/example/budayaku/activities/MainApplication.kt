package com.example.budayaku.activities

import android.app.Application
import android.content.Intent
import com.example.budayaku.MainActivity
import com.google.firebase.auth.FirebaseAuth

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            val intent = Intent(this@MainApplication, UserLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } else {
            startActivity(Intent(this@MainApplication, MainActivity::class.java))
        }
    }
}