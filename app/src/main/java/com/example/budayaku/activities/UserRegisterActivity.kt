package com.example.budayaku.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budayaku.R
import com.example.budayaku.databases.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_register.*

class UserRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val auth = FirebaseAuth.getInstance()

        val username = user_usname.text.toString()
        val email = user_email.text.toString()
        val password = user_password.text.toString()
        val location = user_location.text.toString()

        register_submit.setOnClickListener {

            val user = User(username, "083845703072", email, location)

            // Add a new document with a generated ID
            auth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        tv_linkLogin.setOnClickListener {
            val intent = Intent(this@UserRegisterActivity, UserLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}
