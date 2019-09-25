package com.example.budayaku.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.budayaku.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_account.*

class UserAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            startActivity(Intent(this@UserAccountActivity, UserLoginActivity::class.java))
        } else {
            user_usname.text = currentUser.email
        }
    }
}
