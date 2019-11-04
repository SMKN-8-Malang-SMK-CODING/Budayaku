package com.example.budayaku.activities

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Toast.makeText(this, "MainApplication", Toast.LENGTH_SHORT).show()

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            Toast.makeText(this, "MainApplication User Null", Toast.LENGTH_LONG).show()
            val intent = Intent(this@MainApplication, LoginActivity::class.java)
//            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
//        else {
//            Toast.makeText(this, "MainApplication User ${currentUser.email}", Toast.LENGTH_LONG)
//                .show()
//            val intent = Intent(this@MainApplication, MainActivity::class.java)
////            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            startActivity(intent)
//        }
    }
}