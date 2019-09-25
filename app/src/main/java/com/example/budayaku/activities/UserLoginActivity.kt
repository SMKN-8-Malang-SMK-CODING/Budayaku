package com.example.budayaku.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budayaku.MainActivity
import com.example.budayaku.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_user_login.*

class UserLoginActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        if (FirebaseAuth.getInstance().currentUser != null) {
            moveToHome()
        }

        btn_submit_login.setOnClickListener {
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()

            if (email.isEmpty()) {
                edt_email.error = "Email tidak boleh kosong"
            }

            if (password.isEmpty()) {
                edt_password.error = "Password tidak boleh kosong"
            }

            if (email.isEmpty() || password.isEmpty()) {
                return@setOnClickListener
            }

            loading_login.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(
                email, password
            ).addOnSuccessListener {
                val settings =
                    FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

                firestore.firestoreSettings = settings
                firestore.collection("user").document(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .get()
                    .addOnSuccessListener {
                        moveToHome()
                    }
            }.addOnFailureListener {
                loading_login.visibility = View.GONE
                Toast.makeText(this, "Login Gagal ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        btn_loginClose.setOnClickListener {
            val intent = Intent(this@UserLoginActivity, MainActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        txt_register_here.setOnClickListener {
            startActivity(Intent(this@UserLoginActivity, UserRegisterActivity::class.java))
        }
    }

    private fun moveToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
