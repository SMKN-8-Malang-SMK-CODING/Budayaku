package com.example.budayaku.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budayaku.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val auth = FirebaseAuth.getInstance()

        register_submit.setOnClickListener {
            val username = user_usname.text.toString()
            val email = user_email.text.toString()
            val password = user_password.text.toString()

//          Add a new document with a generated ID
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    val currentUser = auth.currentUser

                    val profileUpdate =
                        UserProfileChangeRequest.Builder().setDisplayName(username).build()

                    currentUser?.updateProfile(profileUpdate)
                        ?.addOnSuccessListener {
                            Toast.makeText(this, "User profile updated", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        tv_linkLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}
