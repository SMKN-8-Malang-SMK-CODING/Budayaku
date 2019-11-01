package com.example.budayaku.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.budayaku.R
import com.example.budayaku.databases.Daerah
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_sub_modul.*

class DetailSubModulActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_sub_modul)

        val jenis = intent.getStringExtra("jenis")
        val id_daerah = intent.getIntExtra("id_daerah", 1)

//        rv_subModul.apply {
//            layoutManager = LinearLayoutManager(this@DetailSubModulActivity, LinearLayoutManager.HORIZONTAL, false)
//            setHasFixedSize(true)
//
//            adapter = AdapterSubModul(listSubModul)
//        }

        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection(jenis!!).whereEqualTo("id_daerah", id_daerah)
        docRef.get()
            .addOnSuccessListener {

                val datas = it.toObjects(Daerah::class.java)

                datas.forEach { document ->

                    if (document != null) {
                        tv_titleSubModul.text = document.name
                        tv_infoSubModul.text = document.info
                    } else {
                        Log.d("notexist", "No such document")
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.d("errordb", "get failed with ", exception)
            }

        iv_backDetail.setOnClickListener {
            onBackPressed()
        }
    }
}
