package com.example.budayaku.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.databases.Modul
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_modul.*

class DetailModulActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_modul)

        val db = FirebaseFirestore.getInstance()
        val id_daerah = intent.getIntExtra("id_daerah", 1)

        val docRef = db.collection("daerah").whereEqualTo("id_daerah", id_daerah)
        docRef.get()
            .addOnSuccessListener {

                val document = it.toObjects(Modul::class.java)[0]

                if (document != null) {
                    tv_modulTitle.text = document.name
                    tv_modulDetail.text = document.info

                    Glide.with(this).load(document.image_url)
                        .apply(RequestOptions())
                        .into(bg_detailModul)
                } else {
                    Log.d("notexist", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("errordb", "get failed with ", exception)
            }

        btn_senjata.setOnClickListener {
            val intent = Intent(this@DetailModulActivity, DetailSubModulActivity::class.java)
            intent.putExtra("jenis", "senjata")
            intent.putExtra("id_daerah", id_daerah)
            startActivity(intent)
        }

        btn_rumah.setOnClickListener {
            val intent = Intent(this@DetailModulActivity, DetailSubModulActivity::class.java)
            intent.putExtra("jenis", "rumah")
            intent.putExtra("id_daerah", id_daerah)
            startActivity(intent)
        }

        btn_tarian.setOnClickListener {
            val intent = Intent(this@DetailModulActivity, DetailSubModulActivity::class.java)
            intent.putExtra("jenis", "tarian")
            intent.putExtra("id_daerah", id_daerah)
            startActivity(intent)
        }

        btn_pakaian.setOnClickListener {
            val intent = Intent(this@DetailModulActivity, DetailSubModulActivity::class.java)
            intent.putExtra("jenis", "pakaian")
            intent.putExtra("id_daerah", id_daerah)
            startActivity(intent)
        }
    }
}
