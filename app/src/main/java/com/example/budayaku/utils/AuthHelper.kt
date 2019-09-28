package com.example.budayaku.utils

import android.app.Activity
import android.content.Intent
import com.example.budayaku.activities.UserLoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthHelper {
    fun loginCheck(context: Activity): FirebaseUser? {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            val intent = Intent(context, UserLoginActivity::class.java)

            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            context.startActivity(intent)

            return null
        }

        return currentUser
    }

    fun signOut(context: Activity) {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(context, UserLoginActivity::class.java)

        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        context.startActivity(intent)
    }
}