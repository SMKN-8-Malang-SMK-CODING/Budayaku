package com.example.budayaku.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.adapters.CommentForumAdapter
import com.example.budayaku.databases.DataCommentForum
import com.example.budayaku.databases.DataForum
import com.example.budayaku.databases.User
import com.example.budayaku.utils.ShimmerHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detail_forum.*

class DetailForumActivity : AppCompatActivity() {

    private val firestore = Firebase.firestore
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var commentForumAdapter: CommentForumAdapter

    private val listComment = ArrayList<DataCommentForum>()

    private val currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_forum)

        val topik = intent.getStringExtra("topik")

        val docRef = db.collection("forum").whereEqualTo("topik", topik)
        docRef.get()
            .addOnSuccessListener {

                val document = it.toObjects(DataForum::class.java)[0]

                if (document != null) {

                    tv_topikDetail.text = document.topik
                    tv_deskripsiDetail.text = document.deskripsi

                    Glide.with(this).load(currentUser?.photoUrl)
                        .apply(RequestOptions())
                        .into(civ_userComment)

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

                            Log.d("errordb", "get failed with ", exception)
                        }
                } else {
                    Log.d("notexist", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("errordb", "get failed with ", exception)
            }

        commentForumAdapter = CommentForumAdapter(this)

        rv_commentDetailForum.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)

            adapter = commentForumAdapter
            isNestedScrollingEnabled = false
        }

        loadDataComment()

        back_detailForum.setOnClickListener { onBackPressed() }

        fab_sendComment.setOnClickListener {
            sendComment()
        }
    }

    private fun loadDataComment() {
        val topik = intent.getStringExtra("topik")

        firestore.collection("comments")
            .whereEqualTo("topik", topik)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { _, error ->
                if (error != null) {
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                firestore.collection("comments")
                    .whereEqualTo("topik", topik)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener {
                        ShimmerHelper.StopShimmer(shimmer_comment)
                        listComment.clear()
                        listComment.addAll(it.toObjects(DataCommentForum::class.java))

                        commentForumAdapter.setModule(listComment)

                        if (it.size() > 0) {
                            tv_noDataComment.visibility = View.GONE
                        } else {
                            tv_noDataComment.visibility = View.VISIBLE
                        }
                    }
            }
    }

    private fun sendComment() {
        val message = et_messageComment.text.toString().trim()
        val topik = intent.getStringExtra("topik")

        et_messageComment.setText("")

        if (message.isNotEmpty()) {
            firestore.collection("comments").add(
                DataCommentForum(
                    currentUser!!.uid,
                    topik!!,
                    message
                )
            )
        }
    }
}
