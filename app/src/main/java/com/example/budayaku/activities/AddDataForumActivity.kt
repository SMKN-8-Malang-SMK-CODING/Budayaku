package com.example.budayaku.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budayaku.R
import com.example.budayaku.databases.DataForum
import com.example.budayaku.fragments.ForumFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_data_forum.*

class AddDataForumActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data_forum)

        btn_addTopik.setOnClickListener {
            load_addDataForum.visibility = View.VISIBLE
            loadBack_addDataForum.visibility = View.VISIBLE

            storeData()
        }

        iv_backAddData.setOnClickListener {
            onBackPressed()
        }
    }

    private fun storeData() {
        val username = auth.currentUser?.displayName.toString()
        val topik = et_topik.text.toString().trim()
        val deskripsi = et_deskripsi.text.toString().trim()
        var user_avatar: String

        db.collection("users").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val data: DataForum? = it.toObject(DataForum::class.java)
                user_avatar = data!!.user_avatar

                if (topik.isNotEmpty() && deskripsi.isNotEmpty()) {
                    try {
                        firestore.collection("forum").add(
                            DataForum(
                                username,
                                topik,
                                deskripsi,
                                user_avatar
                            )
                        ).addOnSuccessListener {
                            Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT)
                                .show()
                            load_addDataForum.visibility = View.GONE
                            loadBack_addDataForum.visibility = View.GONE
                            val intent = Intent(this, ForumFragment::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }.addOnFailureListener { exception: Exception ->
                            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Semua harus diisi", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
