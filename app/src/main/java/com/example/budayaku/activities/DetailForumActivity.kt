package com.example.budayaku.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.databases.DataForum
import com.example.budayaku.databases.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detail_forum.*

class DetailForumActivity : AppCompatActivity() {

    private val firestore = Firebase.firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_forum)

        val topik = intent.getStringExtra("topik")

        val docRef = db.collection("forum").whereEqualTo("topik", topik)
        docRef.get()
            .addOnSuccessListener {

                val document = it.toObjects(DataForum::class.java)[0]

                if (document != null) {
                    iv_backgroundDetailForum.visibility = View.GONE
                    load_detailForum.visibility = View.GONE

                    tv_topikDetail.text = document.topik
                    tv_deskripsiDetail.text = document.deskripsi

                    var date = ""
                    val year = document.timestamp?.year.toString()

                    date += document.timestamp?.date.toString() + "/"
                    date += document.timestamp?.month.toString() + "/"
                    date += year.substring(year.length - 2)

                    tv_timeForumDetail.text = date

                    firestore.collection("users").document(document.user_id).get()
                        .addOnSuccessListener { et ->
                            val user: User = et.toObject()!!

                            tv_usernameForumDetail.text = user.username

                            Glide.with(this).load(user.user_avatar)
                                .apply(RequestOptions())
                                .into(civ_photoUserForumDetail)
                        }.addOnFailureListener { exception ->
                            iv_backgroundDetailForum.visibility = View.GONE
                            load_detailForum.visibility = View.GONE

                            Log.d("errordb", "get failed with ", exception)
                        }
                } else {
                    iv_backgroundDetailForum.visibility = View.GONE
                    load_detailForum.visibility = View.GONE

                    Log.d("notexist", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                iv_backgroundDetailForum.visibility = View.GONE
                load_detailForum.visibility = View.GONE

                Log.d("errordb", "get failed with ", exception)
            }

        back_detailForum.setOnClickListener { onBackPressed() }
    }
}
