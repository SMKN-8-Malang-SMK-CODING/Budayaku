package com.example.budayaku.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budayaku.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        val firestore = Firebase.firestore

        register_submit.setOnClickListener {
            val username = user_usname.text.toString()
            val email = user_email.text.toString()
            val password = user_password.text.toString()

            if (username.isEmpty()) user_usname.error = "Username tidak boleh kosong"
            if (email.isEmpty()) user_email.error = "Email tidak boleh kosong"
            if (password.isEmpty()) user_password.error = "Password tidak boleh kosong"
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) return@setOnClickListener

            reg_block.visibility = View.VISIBLE
            reg_load.visibility = View.VISIBLE

//          Add a new document with a generated ID
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    val currentUser = auth.currentUser

                    val data =
                        hashMapOf(
                            "user_avatar" to "https://firebasestorage.googleapis.com/v0/b/budayaku-6298f.appspot.com/o/User%2Fdefault-profile.png?alt=media&token=06808e07-8e24-467c-9c91-0fd85f54b3e4",
                            "username" to username
                        )

                    firestore.collection("users").document(currentUser!!.uid).set(data)

                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()

                    currentUser.updateProfile(profileUpdate)
                        .addOnSuccessListener {
                            Toast.makeText(this, "User profile updated", Toast.LENGTH_SHORT).show()
                            reg_block.visibility = View.GONE
                            reg_load.visibility = View.GONE
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
