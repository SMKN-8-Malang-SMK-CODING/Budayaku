package com.example.budayaku.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.databases.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_user_account.*

class UserAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("users").document(currentUser.uid).get()
                .addOnSuccessListener {
                    val data: User? = it.toObject(User::class.java)
                    user_usname.text = data?.name
                    user_phone.text = data?.phone
                    user_email.text = data?.email
                    user_location.text = data?.location
                    Glide.with(this).load(data?.user_avatar)
                        .apply(RequestOptions())
                        .into(civ_userPhoto)
                    Glide.with(this).load(data?.user_avatar)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(10, 3)))
                        .into(iv_userPhotoBackground)
                }
        } else {
            startActivity(Intent(this@UserAccountActivity, AccountNotFoundActivity::class.java))
        }

        iv_backUser.setOnClickListener {
            onBackPressed()
        }
    }
}
