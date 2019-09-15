package com.example.budayaku.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budayaku.R
import com.example.budayaku.databases.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login_user.*

class LoginUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        val db = FirebaseFirestore.getInstance()

        // Create a new user with a first and last name
        val user = User("Azhura", "083845703072", "azhura@gmail.com", "Jawa Timur")

        // Add a new document with a generated ID
        btn_submit_login.setOnClickListener {
            db.collection("user").add(user).addOnCompleteListener {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        txt_register_here.setOnClickListener {
            startActivity(Intent(this@LoginUser, RegisterUser::class.java))
        }
    }
}
